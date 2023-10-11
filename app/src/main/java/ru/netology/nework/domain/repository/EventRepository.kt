package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Event

interface EventRepository {
    val data: Flow<List<Event>>

    suspend fun getAll()

    suspend fun saveEvent(event: Event)
    suspend fun removeEventById(id: Int)
    suspend fun likeEventById(id: Int)
    suspend fun unlikeEventById(id: Int)

    suspend fun participateById(id: Int)

    suspend fun unparticipateById(id: Int)

}