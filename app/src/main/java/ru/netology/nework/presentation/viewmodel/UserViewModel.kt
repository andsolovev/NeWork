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
    repository: UserRepository,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {

    val data = repository.data

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

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