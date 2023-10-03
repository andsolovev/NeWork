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
import ru.netology.nework.utils.formatDateTime

class JobAdapter(
    private val onJobInteractionListener: OnJobInteractionListener,
) :
    ListAdapter<Job, JobViewHolder>(JobDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onJobInteractionListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position) ?: return
        holder.bind(job)
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val onJobInteractionListener: OnJobInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) {
        val startJob = formatDateTime(job.start)
        val finishJob = job.finish?.let { formatDateTime(it) } ?: "..."

        with(binding) {
            jobName.text = job.name
            jobPosition.text = job.position
            jobPeriod.text = itemView.context.getString(
                R.string.job_period,
                startJob,
                finishJob
            )

            linkButton.isVisible = !job.link.isNullOrBlank()

            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onJobInteractionListener.onDeleteJob(job)
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
