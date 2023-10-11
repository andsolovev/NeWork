package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Post

interface PostRepository {
    val data: Flow<List<Post>>
    val wall: Flow<List<Post>>
    suspend fun getAll()
    suspend fun getNewerPosts()
    suspend fun getPostsWall(userId: Int)
    suspend fun savePost(post: Post)
    suspend fun removePostById(id: Int)
    suspend fun likePostById(id: Int)
    suspend fun unlikePostById(id: Int)
}