package ru.netology.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.domain.models.Event
import ru.netology.nework.utils.formatDateTime

class EventAdapter : ListAdapter <Event, EventAdapter.EventViewHolder>(DiffCallback) {

    class EventViewHolder(
        private val binding: CardEventBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            with(binding) {
                author.text = event.author
                content.text = event.content
                dateTime.text = formatDateTime(event.published)
                job.text = event.authorJob
                likeButton.text = "${event.likeOwnerIds?.size}"
            }
            Glide.with(binding.avatar)
                .load(event.authorAvatar)
                .timeout(10_000)
                .error(R.drawable.ic_person_24)
                .circleCrop()
                .into(binding.avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val holder = EventViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }
}