package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.model.User
import ru.netology.nework.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend fun getUserById(id: Int) : User {
        return repository.getUserById(id)
    }
}