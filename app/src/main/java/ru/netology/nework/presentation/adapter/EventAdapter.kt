package ru.netology.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.domain.model.AttachmentType
import ru.netology.nework.domain.model.Event
import ru.netology.nework.presentation.adapter.onInteractionListener.OnEventInteractionListener
import ru.netology.nework.utils.formatDateTimeFromUTC

class EventAdapter(
    private val onEventInteractionListener: OnEventInteractionListener
) : ListAdapter<Event, EventViewHolder>(EventDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val holder = EventViewHolder(binding, onEventInteractionListener)
        return holder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onEventInteractionListener: OnEventInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(event: Event) {
        binding.apply {
            author.text = event.author
            content.text = event.content
            published.text = formatDateTimeFromUTC(event.published)
            dateTime.text = formatDateTimeFromUTC(event.datetime)
            type.text = event.type.toString()
            job.text = event.authorJob

            likeButton.text = "${event.likeOwnerIds?.size}"
            likeButton.setOnClickListener {
                onEventInteractionListener.onLike(event)
            }
            likeButton.isChecked = event.participatedByMe

            participantCheckBox.setOnClickListener {
                onEventInteractionListener.onParticipate(event)
            }
            participantCheckBox.isChecked = event.participatedByMe
            participants.text = itemView.context.getString(
                R.string.participants,
                event.participantsIds.size
            )


            avatar.setOnClickListener {
                onEventInteractionListener.onOpenUserProfile(event)
            }

            imageAttachment.isVisible = event.attachment?.type == AttachmentType.IMAGE
            audioGroup.isVisible = event.attachment?.type == AttachmentType.AUDIO
            videoGroup.isVisible = event.attachment?.type == AttachmentType.VIDEO
            buttonPlayVideo.setOnClickListener {
                onEventInteractionListener.onPlayVideo(
                    event,
                    videoView,
                    progressBarVideo,
                    buttonPlayVideo,
                    videoGroup
                )
                progressBarVideo.isVisible = true
                it.isVisible = false
            }
            buttonPlay.setOnClickListener {
                onEventInteractionListener.onPlayAudio(event, audioBar, progressBarAudio)
            }
            buttonPause.setOnClickListener {
                onEventInteractionListener.onPauseAudio(event)
            }
            buttonStop.setOnClickListener {
                onEventInteractionListener.onStopAudio(event)
            }
            link.setOnClickListener {
                event.link?.let { onEventInteractionListener.onLink(event) }
            }

            menuButton.isVisible = event.ownedByMe
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onEventInteractionListener.onRemove(event)
                                true
                            }

                            R.id.edit -> {
                                onEventInteractionListener.onEdit(event)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            imageAttachment.setOnClickListener {
                onEventInteractionListener.onImage(event)
            }

            geoButton.isVisible = event.coords != null

            geoButton.setOnClickListener {
                onEventInteractionListener.onCoords(event)
            }

            Glide.with(avatar)
                .load(event.authorAvatar)
                .timeout(10_000)
                .error(R.drawable.ic_person_24)
                .circleCrop()
                .into(avatar)

            Glide.with(binding.imageAttachment)
                .load(event.attachment?.url)
                .timeout(10_000)
                .error(R.drawable.ic_image_24)
                .into(binding.imageAttachment)
        }
    }
}


object EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem == newItem
}
