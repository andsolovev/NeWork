package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentProfile : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val userId = authViewModel.state.value?.id ?: 0

//        if(userId == 0) {
//            findNavController().navigate(R.id.action_fragmentProfile_to_fragmentSignIn)
//        } else {
//            userViewModel.getUserById(userId)
//        }

        userViewModel.getUserById(userId)

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.name.text = userViewModel.user.value?.name
            Glide.with(binding.avatar)
                .load(userViewModel.user.value?.avatar)
                .error(R.drawable.ic_person_24)
                .timeout(10_000)
                .circleCrop()
                .into(binding.avatar)
        }





//        lifecycleScope.launch(Dispatchers.IO) {
//            userViewModel.getUserById(520)
//        }






//        val avatar = userViewModel.user.value?.avatar
//        val name = userViewModel.user.value?.name







        return binding.root
    }
}