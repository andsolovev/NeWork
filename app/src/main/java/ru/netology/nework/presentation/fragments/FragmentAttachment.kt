package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.netology.nework.databinding.FragmentImageAttachmentBinding

class FragmentAttachment : Fragment() {

    companion object {

        private const val LINK = "LINK"

        var Bundle.link: String?
            set(value) = putString(LINK, value)
            get() = getString(LINK)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageAttachmentBinding.inflate(
            inflater,
            container,
            false
        )

        Glide.with(binding.imageAttachment)
            .load(arguments?.link)
            .timeout(6_000)
            .into(binding.imageAttachment)

        return binding.root
    }
}


















