package ru.netology.nework.domain.usecases

import ru.netology.nework.domain.model.Post
import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class SavePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend fun savePost(post: Post) = repository.savePost(post)
}