package ru.netology.nework.data.database.repository

import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.EventDao
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toEntity
import ru.netology.nework.data.network.api.EventApiService
import ru.netology.nework.domain.repository.EventRepository
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
}