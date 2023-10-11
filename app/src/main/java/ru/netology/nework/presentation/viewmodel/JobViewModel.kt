package ru.netology.nework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.model.Job
import ru.netology.nework.domain.repository.JobRepository
import ru.netology.nework.domain.usecases.job.GetJobsByUserIdUseCase
import ru.netology.nework.domain.usecases.job.RemoveJobByIdUseCase
import ru.netology.nework.domain.usecases.job.SaveJobUseCase
import ru.netology.nework.utils.SingleLiveEvent
import javax.inject.Inject

val emptyJob = Job(
    id = 0,
    name = "",
    position = "",
    start = "",
    finish = null,
    link = null,
)

@HiltViewModel
class JobViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {

    val data = repository.data

    val editedJob = MutableLiveData(emptyJob)

    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: LiveData<Unit>
        get() = _jobCreated

    private val _job = MutableLiveData<List<Job>>()
    val job: LiveData<List<Job>>
        get() = _job

    val getJobsByUserIdUseCase = GetJobsByUserIdUseCase(repository)
    val saveJobUseCase = SaveJobUseCase(repository)
    val removeJobByIdUseCase = RemoveJobByIdUseCase(repository)

    fun getJobsByUserId(userId: Int) {
        viewModelScope.launch {
            _job.value = getJobsByUserIdUseCase.getJobsByUserId(userId)
        }
    }

    fun saveJob() {
        editedJob.value?.let {
            viewModelScope.launch {
                try {
                    saveJobUseCase.saveJob(it)
                    _jobCreated.value = Unit
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        editedJob.value = emptyJob
    }

    fun changeContent(name: String, position: String, jobStart: String, jobFinish: String?, link: String) {
        if (editedJob.value?.name != name ||
            editedJob.value?.position != position ||
            editedJob.value?.start != jobStart ||
            editedJob.value?.finish != jobFinish ||
            editedJob.value?.link != link
            ) {
            editedJob.value = editedJob.value?.copy(
                name = name,
                position = position,
                start = jobStart,
                finish = jobFinish,
                link = link
            )
        }
        return
    }

    fun edit(job: Job) {
        editedJob.value = job
    }

    fun removeJobById(id: Int) = viewModelScope.launch {
        removeJobByIdUseCase.removePostById(id)
    }

    fun vipeJob() = viewModelScope.launch {
        repository.removeJob()
        _job.value = emptyList()
    }





}