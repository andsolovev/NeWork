package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.model.Attachment
import ru.netology.nework.domain.model.Coordinates
import ru.netology.nework.domain.model.Event
import ru.netology.nework.domain.model.EventType
import ru.netology.nework.domain.repository.EventRepository
import ru.netology.nework.domain.usecases.event.GetAllEventsUseCase
import ru.netology.nework.domain.usecases.event.GetNewerEventsUseCase
import ru.netology.nework.domain.usecases.event.LikeEventByIdUseCase
import ru.netology.nework.domain.usecases.event.ParticipateEventByIdUseCase
import ru.netology.nework.domain.usecases.event.RemoveEventByIdUseCase
import ru.netology.nework.domain.usecases.event.SaveEventUseCase
import ru.netology.nework.domain.usecases.event.UnlikeEventByIdUseCase
import ru.netology.nework.domain.usecases.event.UnparticipateEventByIdUseCase
import ru.netology.nework.utils.SingleLiveEvent
import javax.inject.Inject

val emptyEvent = Event()

@HiltViewModel
class EventViewModel @Inject constructor(
    repository: EventRepository,
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val getNewerEventsUseCase: GetNewerEventsUseCase,
    private val saveEventUseCase: SaveEventUseCase,
    private val likeEventByIdUseCase: LikeEventByIdUseCase,
    private val unlikeEventByIdUseCase: UnlikeEventByIdUseCase,
    private val removeEventByIdUseCase: RemoveEventByIdUseCase,
    private val participateEventByIdUseCase: ParticipateEventByIdUseCase,
    private val unparticipateEventByIdUseCase: UnparticipateEventByIdUseCase,
) : ViewModel() {

    val data = repository.data

    val edited = MutableLiveData(emptyEvent)

    private val _eventCreated = SingleLiveEvent<Unit>()
    val eventCreated: LiveData<Unit>
        get() = _eventCreated

    fun saveEvent() {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    saveEventUseCase.saveEvent(it)
                    _eventCreated.value = Unit
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        edited.value = emptyEvent
    }

    fun changeContent(
        content: String,
        link: String,
        attachment: Attachment?,
        dateTime: String,
        type: EventType,
        coords: Coordinates?
    ) {
        val text = content.trim()
        if (edited.value?.content != text || edited.value?.link != link) {
            edited.value = edited.value?.copy(content = text)
            edited.value = edited.value?.copy(link = link)
            edited.value = edited.value?.copy(attachment = attachment)
            edited.value = edited.value?.copy(datetime = dateTime)
            edited.value = edited.value?.copy(type = type)
            edited.value = edited.value?.copy(coords = coords)
        }
        return
    }

    fun edit(event: Event) {
        edited.value = event
    }

    fun removeById(id: Int) = viewModelScope.launch {
        removeEventByIdUseCase.removeEventById(id)
    }

    fun likeById(id: Int) = viewModelScope.launch {
        likeEventByIdUseCase.likeEventById(id)
    }

    fun unlikeById(id: Int) = viewModelScope.launch {
        unlikeEventByIdUseCase.unlikeEventById(id)
    }

    fun participateById(id: Int) = viewModelScope.launch {
        participateEventByIdUseCase.participateEventById(id)
    }

    fun unparticipateById(id: Int) = viewModelScope.launch {
        unparticipateEventByIdUseCase.unparticipateEventById(id)
    }

    fun getAllEvents() = viewModelScope.launch {
        getAllEventsUseCase()
    }

    fun getNewerEvents() = viewModelScope.launch {
        getNewerEventsUseCase()
    }
}