package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.model.User
import ru.netology.nework.domain.repository.UserRepository
import ru.netology.nework.domain.usecases.user.GetAllUsersUseCase
import ru.netology.nework.domain.usecases.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val data = repository.data

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    val getAllUsersUseCase = GetAllUsersUseCase(repository)
    val getUserByIdUseCase = GetUserByIdUseCase(repository)

    fun getAllUsers() = viewModelScope.launch {
        getAllUsersUseCase()
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            _user.value = getUserByIdUseCase.getUserById(id)
        }
    }

    fun vipeUser() {
        viewModelScope.launch {
            _user.value = User()
        }
    }

}