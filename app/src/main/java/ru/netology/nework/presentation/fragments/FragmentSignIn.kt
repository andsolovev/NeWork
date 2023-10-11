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
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignInBinding
import ru.netology.nework.presentation.viewmodel.AuthViewModel

@AndroidEntryPoint
class FragmentSignIn : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            if (binding.login.text.isBlank() || binding.password.text.isBlank()) {
                Toast.makeText(context, "Please enter login and password", Toast.LENGTH_LONG).show()
            } else {
                authViewModel.updateUser(
                    binding.login.text.toString(),
                    binding.password.text.toString()
                )

            }
        }

        authViewModel.state.observe(viewLifecycleOwner) {
            if (authViewModel.authorized) findNavController().navigate(R.id.action_fragment_sign_in_to_fragment_posts)
        }

        binding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
        }

        return binding.root
    }
}