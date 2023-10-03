package ru.netology.nework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.data.database.entity.JobEntity

@Dao
interface JobDao {

    @Query("SELECT * FROM JobEntity ORDER BY id DESC")
    fun getAll(): Flow<List<JobEntity>>

    @Query("SELECT * FROM JobEntity WHERE id = :jobId LIMIT 1")
    suspend fun getJobById(jobId: Int): JobEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(jobEntity: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobEntities: List<JobEntity>)

    @Query("DELETE FROM JobEntity WHERE id = :jobId")
    suspend fun removeJobById(jobId: Int)

    @Query("DELETE FROM JobEntity")
    suspend fun removeAll()
}
