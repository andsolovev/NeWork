package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class UnlikePostByIdUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend fun unlikePostById(id: Int) = repository.unlikePostById(id)
}