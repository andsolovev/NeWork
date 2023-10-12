package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class UnlikeEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend fun unlikeEventById(id: Int) = repository.unlikeEventById(id)
}