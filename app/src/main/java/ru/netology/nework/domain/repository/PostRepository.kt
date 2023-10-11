package ru.netology.nework.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>
    val wall: Flow<List<Post>>
    suspend fun getAll()
    suspend fun getNewerPosts()
    suspend fun getPostsWall(userId: Int)
    suspend fun savePost(post: Post)
    suspend fun removePostById(id: Int)
    suspend fun likePostById(id: Int)
    suspend fun unlikePostById(id: Int)
}