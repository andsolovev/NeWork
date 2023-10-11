package ru.netology.nework.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.ActivityMainBinding
import ru.netology.nework.presentation.fragments.FragmentProfile
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var hostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            bottomNavView.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.bottomNavView

        hostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = hostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_posts,
                R.id.navigation_users,
                R.id.navigation_events,
                R.id.my_profile
            )
        )

        bottomNavView.setupWithNavController(navController)

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_posts -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.fragment_posts)
                    true
                }

                R.id.navigation_users -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.fragment_users)
                    true
                }

                R.id.navigation_events -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.fragmentEvents)
                    true
                }

                R.id.my_profile -> {
                    if (!authViewModel.authorized) {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.fragment_sign_in)
                    } else {
                        lifecycleScope.launch {
                            authViewModel.state.value?.id?.let {
                                userViewModel.getUserById(it)
                            }
                        }
                        findNavController(R.id.nav_host_fragment).navigate(
                            R.id.fragment_profile,
                            FragmentProfile.createArguments(userId = authViewModel.state.value?.id)
                        )
                    }
                    true
                }

                else -> false
            }
        }
    }
}



