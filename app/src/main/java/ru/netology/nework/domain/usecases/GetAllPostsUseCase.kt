package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
){
    suspend fun getAllPosts() {
        repository.getAll()
    }
}