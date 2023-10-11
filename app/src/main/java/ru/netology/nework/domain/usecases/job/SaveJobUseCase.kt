package ru.netology.nework.domain.usecases.job

import ru.netology.nework.domain.model.Job
import ru.netology.nework.domain.repository.JobRepository
import javax.inject.Inject

class SaveJobUseCase @Inject constructor(
    private val repository: JobRepository
) {
    suspend fun saveJob(job: Job) = repository.saveJob(job)
}