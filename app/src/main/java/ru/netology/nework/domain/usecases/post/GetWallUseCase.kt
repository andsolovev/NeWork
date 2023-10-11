package ru.netology.nework.domain.usecases.post

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class GetWallUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend fun getWall(userId: Int) = repository.getPostsWall(userId)
}