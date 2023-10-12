package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.netology.nework.domain.model.User

interface UserApiService {
    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
}