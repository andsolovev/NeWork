package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentPostsBinding
import ru.netology.nework.presentation.adapter.PostAdapter
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.PostViewModel

@AndroidEntryPoint
class FragmentPosts : Fragment() {

    private val postViewModel: PostViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = PostAdapter()

        binding.postList.adapter = adapter

        var menuProvider: MenuProvider? = null

        authViewModel.state.observe(viewLifecycleOwner) {
            menuProvider?.let { requireActivity()::removeMenuProvider }
        }

        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_auth, menu)
                menu.setGroupVisible(R.id.authorized, authViewModel.authorized)
                menu.setGroupVisible(R.id.unauthorized, !authViewModel.authorized)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when(menuItem.itemId) {
                    R.id.menu_signIn -> {
                        findNavController().navigate(R.id.action_fragment_posts_to_fragmentSignIn)
                        true
                    }
                    R.id.menu_signUp -> {
                        findNavController().navigate(R.id.action_fragment_posts_to_fragmentSignUp)
                        true
                    }
                    R.id.menu_myProfile -> {
                        findNavController().navigate(R.id.action_fragment_posts_to_fragmentProfile)
                        true
                    }
                    R.id.menu_logout -> {
                        authViewModel.logOut()
                        true
                    }
                    else -> false

                }
        }.apply {
            menuProvider = this
        }, viewLifecycleOwner)

        lifecycleScope.launch {
            try {
                postViewModel.data.collect {posts ->
                    adapter.submitList(posts)
                    binding.emptyList.isVisible = posts.isEmpty()
                }
            } catch (exception: Exception) {
                return@launch
            }

        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_posts_to_fragmentNewPost)
        }

        return binding.root
    }
}