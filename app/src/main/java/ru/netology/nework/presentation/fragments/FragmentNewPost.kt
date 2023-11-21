package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.domain.model.Attachment
import ru.netology.nework.domain.model.AttachmentType
import ru.netology.nework.presentation.viewmodel.PostViewModel
import ru.netology.nework.presentation.viewmodel.emptyPost
import ru.netology.nework.utils.AndroidUtils

@AndroidEntryPoint
class FragmentNewPost : Fragment() {

    private val postViewModel: PostViewModel by viewModels()

    private var type: AttachmentType? = null
    private var url: String = ""
    private var attachment: Attachment? = null

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
                postButton.text = getString(R.string.save)
            }

            if (postViewModel.edited.value?.attachment != null) {
                url = postViewModel.edited.value?.attachment?.url.toString()
                type = postViewModel.edited.value?.attachment?.type
                attachmentGroup.visibility = View.VISIBLE
                editAttachmentUrl.setText(url)
                addImage.visibility = View.VISIBLE
                addAudio.visibility = View.VISIBLE
                addVideo.visibility = View.VISIBLE
                imageContainer.visibility = View.VISIBLE
                when (type) {
                    AttachmentType.IMAGE -> {
                        Glide.with(binding.image)
                            .load(url)
                            .timeout(10_000)
                            .error(R.drawable.ic_person_24)
                            .into(binding.image)
                    }

                    AttachmentType.AUDIO -> {
                        image.setImageResource(R.drawable.ic_audio_file_24)
                        mediaUrl.visibility = View.VISIBLE
                        mediaUrl.text = getString(
                            R.string.attachment_link,
                            "audio",
                            url
                        )
                    }

                    AttachmentType.VIDEO -> {
                        image.setImageResource(R.drawable.ic_video_24)
                        mediaUrl.visibility = View.VISIBLE
                        mediaUrl.text = getString(
                            R.string.attachment_link,
                            "video",
                            url
                        )
                    }

                    else -> {}
                }
            }

            editText.requestFocus()

            addImage.setOnClickListener {
                type = AttachmentType.IMAGE
                attachmentGroup.visibility = View.VISIBLE
                addAudio.visibility = View.GONE
                addVideo.visibility = View.GONE
            }


            addAudio.setOnClickListener {
                attachmentGroup.visibility = View.VISIBLE
                type = AttachmentType.AUDIO
                addImage.visibility = View.GONE
                addVideo.visibility = View.GONE
            }

            addVideo.setOnClickListener {
                attachmentGroup.visibility = View.VISIBLE
                type = AttachmentType.VIDEO
                addImage.visibility = View.GONE
                addAudio.visibility = View.GONE
            }

            removeUrl.setOnClickListener {
                url = ""
                type = null
                imageContainer.visibility = View.GONE
                attachmentGroup.visibility = View.INVISIBLE
                editAttachmentUrl.setText("")
                addImage.visibility = View.VISIBLE
                addAudio.visibility = View.VISIBLE
                addVideo.visibility = View.VISIBLE
            }

            addUrl.setOnClickListener {
                url = editAttachmentUrl.text.toString()

                type?.takeIf {
                    URLUtil.isValidUrl(url)
                }
                    ?.let {
                        attachment = Attachment(url, it)
                    }

                binding.imageContainer.visibility = View.VISIBLE
                AndroidUtils.hideKeyboard(requireView())
                when (type) {
                    AttachmentType.IMAGE -> {
                        Glide.with(binding.image)
                            .load(url)
                            .timeout(10_000)
                            .error(R.drawable.ic_person_24)
                            .into(binding.image)
                    }

                    AttachmentType.AUDIO -> {
                        image.setImageResource(R.drawable.ic_audio_file_24)
                        mediaUrl.visibility = View.VISIBLE
                        mediaUrl.text = getString(
                            R.string.attachment_link,
                            "audio",
                            url
                        )
                    }

                    AttachmentType.VIDEO -> {
                        image.setImageResource(R.drawable.ic_video_24)
                        mediaUrl.visibility = View.VISIBLE
                        mediaUrl.text = getString(
                            R.string.attachment_link,
                            "video",
                            url
                        )
                    }

                    else -> {}
                }
                attachmentGroup.visibility = View.GONE
            }

            remove.setOnClickListener {
                url = ""
                type = null
                imageContainer.visibility = View.GONE
                attachmentGroup.visibility = View.INVISIBLE
                editAttachmentUrl.setText("")
                addImage.visibility = View.VISIBLE
                addAudio.visibility = View.VISIBLE
                addVideo.visibility = View.VISIBLE
            }

            postButton.setOnClickListener {
                with(binding) {
                    if (editText.text.isEmpty()) {
                        Toast.makeText(context, "No text!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        postViewModel.changeContent(
                            editText.text.toString(),
                            editLink.text.toString(),
                            attachment
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
