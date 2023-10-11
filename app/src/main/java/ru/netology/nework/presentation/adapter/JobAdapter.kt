package ru.netology.nework.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.domain.model.Job
import ru.netology.nework.presentation.adapter.onInteractionListener.OnJobInteractionListener
import ru.netology.nework.utils.formatDateJobFromUTC

class JobAdapter(
    private val onJobInteractionListener: OnJobInteractionListener,
    private val ownedByMe: Boolean
) :
    ListAdapter<Job, JobViewHolder>(JobDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onJobInteractionListener, ownedByMe)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position) ?: return
        holder.bind(job)
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val onJobInteractionListener: OnJobInteractionListener,
    private val ownedByMe: Boolean
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) {
        val startJob = formatDateJobFromUTC(job.start)
        val finishJob = job.finish?.let { formatDateJobFromUTC(it) } ?: "..."

        with(binding) {
            jobName.text = job.name
            jobPosition.text = job.position
            jobPeriod.text = itemView.context.getString(
                R.string.job_period,
                startJob,
                finishJob
            )

            linkButton.isVisible = !job.link.isNullOrBlank()
            linkButton.setOnClickListener {
                job.link?.let { onJobInteractionListener.onLink(job) }
            }

            menuButton.isVisible = ownedByMe
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onJobInteractionListener.onRemoveJob(job)
                                true
                            }

                            R.id.edit -> {
                                onJobInteractionListener.onEditJob(job)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }

    }
}

object JobDiffItemCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}
