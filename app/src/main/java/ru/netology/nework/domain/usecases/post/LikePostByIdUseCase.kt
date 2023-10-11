package ru.netology.nework.domain.usecases.post

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class LikePostByIdUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend fun likePostById(id: Int) = repository.likePostById(id)
}