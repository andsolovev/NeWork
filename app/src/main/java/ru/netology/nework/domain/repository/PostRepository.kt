package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    suspend fun savePost(post: Post)
    suspend fun removePostById(id: Int)
    suspend fun likePostById(id: Int)
    suspend fun unlikePostById(id: Int)
}