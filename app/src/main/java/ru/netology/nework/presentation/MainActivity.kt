package ru.netology.nework.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.presentation.fragments.FragmentEvents
import ru.netology.nework.presentation.fragments.FragmentPosts
import ru.netology.nework.presentation.fragments.FragmentProfile
import ru.netology.nework.presentation.fragments.FragmentSignIn
import ru.netology.nework.presentation.fragments.FragmentUsers
import ru.netology.nework.presentation.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav_view) as BottomNavigationView

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_posts -> {
                    loadFragment(FragmentPosts())
                    true
                }

                R.id.navigation_users -> {
                    loadFragment(FragmentUsers())
                    true
                }

                R.id.navigation_events -> {
                    loadFragment(FragmentEvents())
                    true
                }

                R.id.my_profile -> {
                    if(!authViewModel.authorized) {
                        loadFragment(FragmentSignIn())
                    }
                    else loadFragment(FragmentProfile())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
    }

}



