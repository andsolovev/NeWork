package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.models.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
}