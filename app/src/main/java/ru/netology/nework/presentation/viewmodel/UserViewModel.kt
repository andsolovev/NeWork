package ru.netology.nework.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.repository.UserRepository
import ru.netology.nework.domain.usecases.GetAllUsersUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val data = repository.data

    val getAllUsersUseCase = GetAllUsersUseCase(repository)

    fun getAllUsers() = viewModelScope.launch {
        Log.d("GAU", "Request sending")
        getAllUsersUseCase.getAllUsers()
        Log.d("GAU", "Request sent")
    }

}