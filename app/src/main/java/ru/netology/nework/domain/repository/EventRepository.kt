package ru.netology.nework.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.domain.model.Event

interface EventRepository {
    val data: Flow<List<Event>>

    suspend fun getAll()

}