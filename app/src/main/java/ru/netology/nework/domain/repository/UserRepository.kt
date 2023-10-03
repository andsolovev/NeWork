package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.User

interface UserRepository {
    val data: Flow<List<User>>

    suspend fun getAll()
    suspend fun getUserById(id: Int) : User
}