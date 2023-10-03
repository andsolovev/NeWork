package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.presentation.viewmodel.PostViewModel
import ru.netology.nework.presentation.viewmodel.emptyPost

@AndroidEntryPoint
class FragmentNewPost : Fragment() {

    private val postViewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding.apply {
            if (postViewModel.edited.value != emptyPost) {
                editText.setText(postViewModel.edited.value?.content)
                editLink.setText(postViewModel.edited.value?.link)
            }
            postButton.setOnClickListener {
                with(binding) {
                    if (editText.text.isEmpty()) {
                        Toast.makeText(context, "No text!", Toast.LENGTH_SHORT)
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
        }

        postViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
