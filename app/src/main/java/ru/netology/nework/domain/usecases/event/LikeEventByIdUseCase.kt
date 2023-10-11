package ru.netology.nework.domain.usecases.event

import ru.netology.nework.domain.repository.EventRepository
import javax.inject.Inject

class LikeEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
){
    suspend fun likeEventById(id: Int) = repository.likeEventById(id)
}