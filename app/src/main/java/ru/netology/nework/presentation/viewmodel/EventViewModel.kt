package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.repository.EventRepository
import ru.netology.nework.domain.usecases.GetAllEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val data = repository.data

    val getAllEventsUseCase = GetAllEventsUseCase(repository)

    fun getAllEvents() = viewModelScope.launch {
        getAllEventsUseCase()
    }
}