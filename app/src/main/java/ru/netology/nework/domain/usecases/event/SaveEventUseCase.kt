package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.model.Event
import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend fun saveEvent(event: Event) = repository.saveEvent(event)
}