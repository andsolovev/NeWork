package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.presentation.adapter.UserAdapter
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentUsers : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = UserAdapter()

        binding.userList.adapter = adapter

        binding.fab.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getAllUsers()

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.data.collectLatest {users ->
                adapter.submitList(users)
                binding.emptyList.isVisible = users.isEmpty()
            }
        }
    return binding.root
    }


}