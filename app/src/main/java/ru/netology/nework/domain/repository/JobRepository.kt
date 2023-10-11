package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Job

interface JobRepository {
    val data: Flow<List<Job>>
    suspend fun getJobsByUserId(userId: Int) : List<Job>
    suspend fun saveJob(job: Job)
    suspend fun removeJobById(id: Int)
    suspend fun removeJob()
}