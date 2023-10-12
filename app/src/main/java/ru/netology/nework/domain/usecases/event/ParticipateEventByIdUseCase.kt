package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class ParticipateEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend fun participateEventById(id: Int) = repository.participateById(id)
}