package ru.netology.nework.domain.usecases.job

import ru.netology.nework.domain.repository.JobRepository
import javax.inject.Inject

class RemoveJobByIdUseCase @Inject constructor(
    private val repository: JobRepository
){
    suspend fun removePostById(id: Int) = repository.removeJobById(id)
}