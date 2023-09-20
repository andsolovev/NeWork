package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.databinding.FragmentPostsBinding
import ru.netology.nework.presentation.adapter.PostAdapter
import ru.netology.nework.presentation.viewmodel.PostViewModel

@AndroidEntryPoint
class FragmentPosts : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = PostAdapter()

        binding.postList.adapter = adapter

        viewModel.getAllPosts()

//        viewModel.data.observe(viewLifecycleOwner) { state ->
//            adapter.submitList(state.posts)
//            binding.emptyText.isVisible = state.empty
//        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.data.collectLatest {posts ->
                adapter.submitList(posts)
                binding.emptyList.isVisible = posts.isEmpty()
            }
        }
//
//        lifecycleScope.launch {
//            viewModel.data.collect {
//                adapter.submitList(it)
//            }
//        }

        return binding.root
    }
}