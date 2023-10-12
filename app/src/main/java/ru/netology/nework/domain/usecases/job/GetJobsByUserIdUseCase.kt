package ru.netology.nework.domain.usecases.job

import ru.netology.nework.domain.model.Job
import ru.netology.nework.domain.repository.JobRepository
import javax.inject.Inject

class GetJobsByUserIdUseCase @Inject constructor(
    private val jobRepository: JobRepository
) {

    suspend fun getJobsByUserId(userId: Int): List<Job> = jobRepository.getJobsByUserId(userId)
}