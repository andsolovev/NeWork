package ru.netology.nework.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.JobDao
import ru.netology.nework.data.database.entity.JobEntity
import ru.netology.nework.data.database.entity.fromDto
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.network.api.JobApiService
import ru.netology.nework.domain.model.Job
import ru.netology.nework.domain.repository.JobRepository
import ru.netology.nework.utils.ApiException
import ru.netology.nework.utils.DbException
import ru.netology.nework.utils.NetworkException
import ru.netology.nework.utils.UnknownException
import java.io.IOException
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImpl @Inject constructor(
    private val dao: JobDao,
    private val apiService: JobApiService
) : JobRepository {

    override val data: Flow<List<Job>> = dao.getAll()
        .map { it.toDto() }

    override suspend fun getJobsByUserId(userId: Int) {
        try {
            dao.removeAll()
            val response = apiService.getJobsByUserId(userId)
            if (!response.isSuccessful) throw ApiException(response.code(), response.message())
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insertJobs(body.fromDto())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: SQLException) {
            throw DbException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveJob(job: Job) {
        try {
            val response = apiService.saveMyJob(job)
            if (!response.isSuccessful) throw ApiException(response.code(), response.message())
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            dao.insertJob(JobEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: SQLException) {
            throw DbException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun deleteJobById(id: Int) {
        TODO("Not yet implemented")
    }

}