package ru.netology.nework.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.domain.models.AttachmentType

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
    @TypeConverter
    fun fromSet(value: Set<Int>): String = value.joinToString()
    @TypeConverter
    fun toSet(value: String): Set<Int> = if (value.isBlank()) {
        emptySet()
    } else value.split(", ").map { it.toInt() }.toSet()
}