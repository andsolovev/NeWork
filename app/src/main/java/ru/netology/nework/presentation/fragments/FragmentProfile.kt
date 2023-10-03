package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.domain.model.Job
import ru.netology.nework.presentation.adapter.JobAdapter
import ru.netology.nework.presentation.adapter.onInteractionListener.OnJobInteractionListener
import ru.netology.nework.presentation.viewmodel.JobViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentProfile : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val userId = userViewModel.user.value?.id ?: 0

        jobViewModel.getJobsByUserId(userId)

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        val jobAdapter = JobAdapter(object : OnJobInteractionListener {
            override fun onEditJob(job: Job) {
                TODO("Not yet implemented")
            }

            override fun onDeleteJob(job: Job) {
                TODO("Not yet implemented")
            }
        })

        binding.jobList.adapter = jobAdapter

        lifecycleScope.launch {
            jobViewModel.data.collectLatest {
                jobAdapter.submitList(it)
            }


        }

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.name.text = userViewModel.user.value?.name
            Glide.with(binding.avatar)
                .load(userViewModel.user.value?.avatar)
                .error(R.drawable.ic_person_24)
                .timeout(10_000)
                .into(binding.avatar)
        }

        return binding.root
    }
}