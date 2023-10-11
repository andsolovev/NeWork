package ru.netology.nework.presentation.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewEventBinding
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.domain.model.Attachment
import ru.netology.nework.domain.model.AttachmentType
import ru.netology.nework.domain.model.Coordinates
import ru.netology.nework.domain.model.EventType
import ru.netology.nework.presentation.viewmodel.EventViewModel
import ru.netology.nework.presentation.viewmodel.PostViewModel
import ru.netology.nework.presentation.viewmodel.emptyEvent
import ru.netology.nework.presentation.viewmodel.emptyPost
import ru.netology.nework.utils.AndroidUtils
import ru.netology.nework.utils.pickDate

@AndroidEntryPoint
class FragmentNewEvent : Fragment() {

    private val eventViewModel: EventViewModel by activityViewModels()

    var type: AttachmentType? = null
    var url: String = ""
    var eventType: EventType = EventType.OFFLINE
    var attachment: Attachment? = null
    var coordinates: Coordinates? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

        binding.apply {
            if (eventViewModel.edited.value != emptyEvent) {
                editText.setText(eventViewModel.edited.value?.content)
                editLink.setText(eventViewModel.edited.value?.link)
                editLat.setText(eventViewModel.edited.value?.coords?.lat)
                editLong.setText(eventViewModel.edited.value?.coords?.long)
                editEventDate.setText(eventViewModel.edited.value?.datetime)
                addButton.text = getString(R.string.save)
            }

            if (eventViewModel.edited.value?.attachment != null) {
                url = eventViewModel.edited.value?.attachment?.url.toString()
                type = eventViewModel.edited.value?.attachment?.type
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
                if (type != null && url != "" && URLUtil.isValidUrl(url)) {
                    attachment = Attachment(url, type!!)
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

            addButton.setOnClickListener {
                eventType = if(buttonOnline.isChecked) EventType.ONLINE else EventType.OFFLINE
                val lat = editLat.text.toString()
                val long= editLong.text.toString()
                coordinates = Coordinates(lat,long)
                Log.d("coords!", "$coordinates, $lat, $long")


                with(binding) {
                    if (editText.text.isEmpty()) {
                        Toast.makeText(context, "No text!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        eventViewModel.changeContent(
                            editText.text.toString(),
                            editLink.text.toString(),
                            attachment,
                            editEventDate.text.toString(),
                            eventType,
                            coordinates
                        )
                        eventViewModel.saveEvent()
                    }

                }
            }

            editEventDate.apply {
                inputType = InputType.TYPE_NULL
                setOnClickListener {
                    pickDate(editEventDate, context, parentFragmentManager)
                }
            }


        }

        eventViewModel.eventCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
