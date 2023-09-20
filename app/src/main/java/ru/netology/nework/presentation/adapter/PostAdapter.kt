package ru.netology.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.domain.models.Post

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(DiffCallback) {

    class PostViewHolder(
        private val binding: CardPostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                author.text = post.author
                content.text = post.content
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val holder = PostViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}