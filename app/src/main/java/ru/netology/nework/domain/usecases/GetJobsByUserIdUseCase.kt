package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.repository.JobRepository
import javax.inject.Inject

class GetJobsByUserIdUseCase @Inject constructor(
    private val jobRepository: JobRepository
) {

    suspend fun getJobsByUserId(userId: Int) = jobRepository.getJobsByUserId(userId)
}