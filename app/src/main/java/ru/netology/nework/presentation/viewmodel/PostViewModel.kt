package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.repository.PostRepository
import ru.netology.nework.domain.usecases.GetAllPostsUseCase
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    val getAllPostsUseCase = GetAllPostsUseCase(repository)

    val data = repository.data

    fun getAllPosts() = viewModelScope.launch {
        getAllPostsUseCase.getAllPosts()
    }
}