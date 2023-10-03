package ru.netology.nework.presentation.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignUpBinding
import ru.netology.nework.presentation.viewmodel.AuthViewModel

@AndroidEntryPoint
class FragmentSignUp : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val photoPickerContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when(it.resultCode) {
            ImagePicker.RESULT_ERROR -> Toast.makeText(
                requireContext(),
                "photo_pic_error",
                Toast.LENGTH_SHORT
            ).show()
            Activity.RESULT_OK -> {
                val uri = it.data?.data ?: return@registerForActivityResult
                authViewModel.changePhoto(uri, uri.toFile())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(photoPickerContract::launch)
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(photoPickerContract::launch)
        }

        authViewModel.photo.observe(viewLifecycleOwner) {
            if (it?.uri == null) {
                binding.photoContainer.visibility = View.GONE
                return@observe
            }

            binding.photoContainer.visibility = View.VISIBLE
            binding.photo.setImageURI(it.uri)
        }

        binding.removePhoto.setOnClickListener {
            authViewModel.removePhoto()
        }

        binding.signUpButton.setOnClickListener {
            if (binding.name.text.isBlank() || binding.login.text.isBlank() || binding.password.text.isBlank() || binding.passwordConfirmation.text.isBlank()) {
                Toast.makeText(context, R.string.please_fill_in_all_fields, Toast.LENGTH_LONG)
                    .show()
            } else if (binding.password.text.toString() != binding.passwordConfirmation.text.toString()) {
                Toast.makeText(context, R.string.passwords_do_not_match, Toast.LENGTH_LONG).show()
            } else {
                authViewModel.registerUser(
                        binding.login.text.toString(),
                        binding.password.text.toString(),
                        binding.name.text.toString(),
                        authViewModel.photo.value?.file
                    )
                authViewModel.state.observe(viewLifecycleOwner) {
                    if(it.id != 0) {
                        Toast.makeText(
                            context,
                            R.string.you_have_successfully_registered,
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_fragmentSignUp_to_fragment_posts)
                    }
                }

            }
        }

        return binding.root
    }
}