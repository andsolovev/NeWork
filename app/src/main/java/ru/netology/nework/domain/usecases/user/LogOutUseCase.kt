package ru.netology.nework.domain.usecases.user

import ru.netology.nework.data.network.authentification.AppAuth
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val appAuth: AppAuth
) {

    operator fun invoke() = appAuth.removeAuth()
}