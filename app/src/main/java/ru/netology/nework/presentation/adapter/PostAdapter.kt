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
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.domain.model.AttachmentType
import ru.netology.nework.domain.model.Post
import ru.netology.nework.presentation.adapter.onInteractionListener.OnPostInteractionListener
import ru.netology.nework.utils.formatDateTimeFromUTC

class PostAdapter(
    private val onPostInteractionListener: OnPostInteractionListener,
) : ListAdapter<Post, PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, onPostInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onPostInteractionListener: OnPostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            content.text = post.content
            link.text = post.link
            dateTime.text = formatDateTimeFromUTC(post.published)
            job.text = post.authorJob
            likeButton.text = "${post.likeOwnerIds?.size}"
            likeButton.setOnClickListener {
                onPostInteractionListener.onLike(post)
            }
            likeButton.isChecked = post.likedByMe
            avatar.setOnClickListener {
                onPostInteractionListener.onOpenUserProfile(post)
            }
            imageAttachment.isVisible = post.attachment?.type == AttachmentType.IMAGE
            audioGroup.isVisible = post.attachment?.type == AttachmentType.AUDIO
            videoGroup.isVisible = post.attachment?.type == AttachmentType.VIDEO
            buttonPlayVideo.setOnClickListener {
                onPostInteractionListener.onPlayVideo(
                    post,
                    videoView,
                    progressBarVideo,
                    buttonPlayVideo,
                    videoGroup
                )
                progressBarVideo.isVisible = true
                it.isVisible = false
            }
            buttonPlay.setOnClickListener {
                onPostInteractionListener.onPlayAudio(post, audioBar, progressBarAudio)
            }
            buttonPause.setOnClickListener {
                onPostInteractionListener.onPauseAudio(post)
            }
            buttonStop.setOnClickListener {
                onPostInteractionListener.onStopAudio(post)
            }
            link.setOnClickListener {
                post.link?.let { onPostInteractionListener.onLink(post) }
            }



            menuButton.isVisible = post.ownedByMe
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onPostInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onPostInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
            Glide.with(binding.avatar)
                .load(post.authorAvatar)
                .timeout(10_000)
                .error(R.drawable.ic_person_24)
                .circleCrop()
                .into(binding.avatar)

            Glide.with(binding.imageAttachment)
                .load(post.attachment?.url)
                .timeout(10_000)
                .error(R.drawable.ic_image_24)
                .into(binding.imageAttachment)
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem
}
