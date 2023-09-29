package ru.netology.nework.data.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toEntity
import ru.netology.nework.data.network.api.PostsApiService
import ru.netology.nework.domain.models.Post
import ru.netology.nework.domain.repository.PostRepository
import ru.netology.nework.utils.ApiException
import ru.netology.nework.utils.NetworkException
import java.io.IOException
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
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw Exception("Body is null")
            dao.insert(body.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun savePost(post: Post) {
        try {
            val response = apiService.savePost(post)
            if(!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}