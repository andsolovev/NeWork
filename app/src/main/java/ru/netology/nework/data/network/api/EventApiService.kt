package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nework.domain.model.Event

interface EventApiService {
    @GET("events")
    suspend fun getAllEvents() : Response<List<Event>>

    @POST("events")
    suspend fun saveEvent(@Body post: Event) : Response<Event>

    @DELETE("events/{event_id}")
    suspend fun removeEventById(@Path("event_id") id: Int): Response<Unit>

    @POST("events/{event_id}/likes")
    suspend fun likeEventById(@Path("event_id") id: Int): Response<Event>

    @DELETE("events/{event_id}/likes")
    suspend fun unlikeEventById(@Path("event_id") id: Int): Response<Event>

    @POST("events/{event_id}/participants")
    suspend fun participateById(@Path("event_id") id: Int): Response<Event>

    @DELETE("events/{event_id}/participants")
    suspend fun unparticipateById(@Path("event_id") id: Int): Response<Event>

}