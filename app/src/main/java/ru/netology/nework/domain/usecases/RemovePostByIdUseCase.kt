package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class RemovePostByIdUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend fun removePostById(id: Int) = repository.removePostById(id)
}