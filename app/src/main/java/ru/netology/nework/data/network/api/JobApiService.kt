package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.*
import ru.netology.nework.domain.model.Job

interface JobApiService {
    @GET("/api/{userId}/jobs")
    suspend fun getJobsByUserId(@Path("userId")userId: Int) : Response<List<Job>>
    @POST("my/jobs")
    suspend fun saveMyJob(@Body job: Job): Response<Job>
    @DELETE("my/jobs/{id}")
    suspend fun removeMyJobById(@Path("id") id: Int): Response<Unit>
}
