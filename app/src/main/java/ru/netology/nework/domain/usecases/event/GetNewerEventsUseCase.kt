package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class GetNewerEventsUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend operator fun invoke() = repository.getNewerEvents()
}