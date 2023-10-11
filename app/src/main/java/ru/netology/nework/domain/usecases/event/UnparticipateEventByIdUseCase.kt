package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class UnparticipateEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend fun unparticipateEventById(id: Int) = repository.unparticipateById(id)
}