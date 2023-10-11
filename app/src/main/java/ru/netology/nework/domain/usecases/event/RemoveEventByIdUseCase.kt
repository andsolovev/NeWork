package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class RemoveEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend fun removeEventById(id: Int) = repository.removeEventById(id)
}