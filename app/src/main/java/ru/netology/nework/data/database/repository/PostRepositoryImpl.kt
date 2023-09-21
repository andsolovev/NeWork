package ru.netology.nework.data.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toEntity
import ru.netology.nework.data.network.api.PostsApiService
import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: PostsApiService
) : PostRepository {
    override val data = dao.getAll()
        .map { it.toDto() }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = apiService.getPosts()
            if(!response.isSuccessful) {
                throw Exception("Response is unsuccessful")
            }
            val body = response.body() ?: throw Exception("Body is null")
            dao.insert(body.toEntity())
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}