package ru.netology.nework.domain.usecases.user

import ru.netology.nework.data.network.authentification.AppAuth
import java.io.File
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val appAuth: AppAuth
) {

    val state = appAuth.state

    suspend fun registerUser(login: String, password: String, name: String, file: File?) {
        appAuth.registerUser(login, password, name, file)
    }
}