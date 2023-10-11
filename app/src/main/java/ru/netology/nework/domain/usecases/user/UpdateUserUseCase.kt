package ru.netology.nework.domain.usecases.user

import ru.netology.nework.data.network.authentification.AppAuth
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val appAuth: AppAuth
) {

    suspend fun updateUser(login: String, password: String) {
        appAuth.updateUser(login, password)
    }
}