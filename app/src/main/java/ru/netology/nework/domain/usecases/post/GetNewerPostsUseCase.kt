package ru.netology.nework.domain.usecases.post

import ru.netology.nework.domain.repository.PostRepository
import javax.inject.Inject

class GetNewerPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke() = repository.getNewerPosts()
}