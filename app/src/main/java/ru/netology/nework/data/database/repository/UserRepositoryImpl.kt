package ru.netology.nework.data.database.repository

import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.UserDao
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toUserEntity
import ru.netology.nework.data.network.api.UserApiService
import ru.netology.nework.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val apiService: UserApiService
) : UserRepository {
    override val data = dao.getAll()
        .map { it.toDto() }
    override suspend fun getAll() {
        try {
            val response = apiService.getAllUsers()
            if (!response.isSuccessful) throw Exception("Response is unsuccessful")
            val body = response.body() ?: throw Exception("Body is null")
            dao.insert(body.toUserEntity())
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}