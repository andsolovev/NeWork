package ru.netology.nework.data.database.repository

import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.EventDao
import ru.netology.nework.data.database.entity.EventEntity
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toEntity
import ru.netology.nework.data.network.api.EventApiService
import ru.netology.nework.domain.model.Event
import ru.netology.nework.domain.repository.EventRepository
import ru.netology.nework.utils.ApiException
import ru.netology.nework.utils.NetworkException
import ru.netology.nework.utils.UnknownException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val dao: EventDao,
    private val apiService: EventApiService
) : EventRepository {
    override val data = dao.getAll()
        .map { it.toDto() }

    override suspend fun getAll() {
        try {
            val response = apiService.getAllEvents()
            if (!response.isSuccessful) throw Exception("Response is unsuccessful")
            val body = response.body() ?: throw Exception("Body is null")
            dao.insert(body.toEntity())
        } catch (_: Exception) {
            return
        }
    }

    override suspend fun getNewerEvents() {
        try {
            val postId = dao.max() ?: 0
            val response = apiService.getNewer(postId)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveEvent(event: Event) {
        try {
            val response = apiService.saveEvent(event)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeEventById(id: Int) {
        try {
            val response = apiService.removeEventById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            dao.removeEventById(id)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun likeEventById(id: Int) {
        try {
            val response = apiService.likeEventById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun unlikeEventById(id: Int) {
        try {
            val response = apiService.unlikeEventById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun participateById(id: Int) {
        try {
            val response = apiService.participateById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun unparticipateById(id: Int) {
        try {
            val response = apiService.unparticipateById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}