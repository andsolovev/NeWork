package ru.netology.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.domain.model.User
import ru.netology.nework.presentation.adapter.onInteractionListener.OnUserInteractionListener

class UserAdapter(
    private val onUserInteractionListener: OnUserInteractionListener
) : ListAdapter<User, UserViewHolder>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val holder = UserViewHolder(binding, onUserInteractionListener)
        return holder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class UserViewHolder(
    private val binding: CardUserBinding,
    private val onUserInteractionListener: OnUserInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) {
        with(binding) {
            userName.text = user.name
            cardUser.setOnClickListener {
                onUserInteractionListener.onOpenProfile(user)
            }

        }
        Glide.with(binding.avatar)
            .load(user.avatar)
            .timeout(10_000)
            .error(R.drawable.ic_person_24)
            .circleCrop()
            .into(binding.avatar)
    }
}

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
