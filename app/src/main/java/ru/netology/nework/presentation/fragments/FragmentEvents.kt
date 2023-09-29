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
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.presentation.adapter.EventAdapter
import ru.netology.nework.presentation.viewmodel.EventViewModel

@AndroidEntryPoint
class FragmentEvents : Fragment() {
    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = EventAdapter()

        binding.userList.adapter = adapter

        viewModel.getAllEvents()

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.data.collectLatest {events ->
                adapter.submitList(events)
                binding.emptyList.isVisible = events.isEmpty()
            }
        }
    return binding.root
    }

}