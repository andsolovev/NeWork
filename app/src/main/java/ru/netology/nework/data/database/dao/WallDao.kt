package ru.netology.nework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.data.database.entity.PostWallEntity

@Dao
interface WallDao {
    @Query("SELECT * FROM PostWallEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostWallEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostWallEntity>)

    @Query("DELETE FROM PostWallEntity")
    suspend fun removeAll()
}
