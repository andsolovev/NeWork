package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewJobBinding
import ru.netology.nework.presentation.viewmodel.JobViewModel
import ru.netology.nework.presentation.viewmodel.emptyJob
import ru.netology.nework.utils.formatDateJobFromUTC
import ru.netology.nework.utils.pickDate

@AndroidEntryPoint
class FragmentNewJob : Fragment() {

    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewJobBinding.inflate(inflater, container, false)

        binding.apply {
            if (jobViewModel.editedJob.value != emptyJob) {
                name.setText(jobViewModel.editedJob.value?.name)
                position.setText(jobViewModel.editedJob.value?.position)
                editJobStart.setText(
                    formatDateJobFromUTC(jobViewModel.editedJob.value?.start)
                )
                jobViewModel.editedJob.value?.finish?.let {
                    editJobFinish.setText(formatDateJobFromUTC(it))
                }
                link.setText(jobViewModel.editedJob.value?.link)
                addButton.text = getString(R.string.save)
            }

            addButton.setOnClickListener {
                with(binding) {
                    if (name.text.isEmpty()) {
                        Toast.makeText(context, "No text!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        jobViewModel.changeContent(
                            name.text.toString(),
                            position.text.toString(),
                            editJobStart.text.toString(),
                            editJobFinish.text.toString().ifBlank { null },
                            link.text.toString()
                        )
                        jobViewModel.saveJob()
                    }
                }
            }

            editJobStart.apply {
                inputType = InputType.TYPE_NULL
                setOnClickListener {
                    pickDate(editJobStart, context, parentFragmentManager)
                }
            }

            editJobFinish.apply {
                inputType = InputType.TYPE_NULL
                setOnClickListener {
                    pickDate(editJobFinish, context, parentFragmentManager)
                }
            }


        }

        jobViewModel.jobCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
