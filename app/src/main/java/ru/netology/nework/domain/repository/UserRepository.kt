package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.models.User

interface UserRepository {
    val data: Flow<List<User>>

    suspend fun getAll()
    suspend fun getUserById(id: Int) : User
}