package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import ru.netology.nework.domain.model.Event

interface EventApiService {
    @GET("events")
    suspend fun getAllEvents() : Response<List<Event>>

}