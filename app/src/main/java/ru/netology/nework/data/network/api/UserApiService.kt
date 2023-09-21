package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import ru.netology.nework.domain.models.User

interface UserApiService {
    @GET("users")
    suspend fun getAllUsers() : Response<List<User>>
}