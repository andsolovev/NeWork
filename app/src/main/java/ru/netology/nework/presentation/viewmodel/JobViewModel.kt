package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.repository.JobRepository
import ru.netology.nework.domain.usecases.GetJobsByUserIdUseCase
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {

    val data = repository.data

    val getJobsByUserIdUseCase = GetJobsByUserIdUseCase(repository)

    fun getJobsByUserId(userId: Int) {
        viewModelScope.launch {
            getJobsByUserIdUseCase.getJobsByUserId(userId)
        }
    }



}