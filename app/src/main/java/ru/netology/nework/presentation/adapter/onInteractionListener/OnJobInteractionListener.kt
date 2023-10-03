package ru.netology.nework.presentation.adapter.onInteractionListener

import ru.netology.nework.domain.model.Job
interface OnJobInteractionListener {
    fun onEditJob(job: Job)
    fun onDeleteJob(job: Job)
}