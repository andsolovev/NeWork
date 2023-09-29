package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.presentation.viewmodel.PostViewModel

@AndroidEntryPoint
class FragmentNewPost : Fragment() {

    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding.postButton.setOnClickListener {
            with(binding) {
                if (editLink.text.isEmpty()) {
                    Toast.makeText(context, "No link!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                postViewModel.changeContent(
                    editText.text.toString(),
                    editLink.text.toString()
                )
                    postViewModel.savePost()
                }

            }
        }

        postViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}