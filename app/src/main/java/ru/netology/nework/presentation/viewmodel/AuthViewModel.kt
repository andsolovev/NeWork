package ru.netology.nework.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.model.Photo
import ru.netology.nework.domain.usecases.user.LogOutUseCase
import ru.netology.nework.domain.usecases.user.RegisterUserUseCase
import ru.netology.nework.domain.usecases.user.UpdateUserUseCase
import ru.netology.nework.utils.SingleLiveEvent
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    val state = registerUserUseCase.state.asLiveData()

    val authorized: Boolean
        get() = state.value?.id != 0

    private val _error = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    val noPhoto = Photo()

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<Photo>
        get() = _photo

    fun registerUser(login: String, password: String, name: String, file: File?) =
        viewModelScope.launch {
            registerUserUseCase.registerUser(login, password, name, file)
        }

    fun updateUser(login: String, password: String) =
        viewModelScope.launch {
            updateUserUseCase.updateUser(login, password)
        }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = Photo(uri, file)
    }

    fun removePhoto() {
        _photo.value = noPhoto
    }

    fun logOut() = logOutUseCase()
}