package ru.netology.nework.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nework.data.database.dao.EventDao
import ru.netology.nework.data.database.dao.JobDao
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.dao.UserDao
import ru.netology.nework.data.database.entity.EventEntity
import ru.netology.nework.data.database.entity.JobEntity
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.UserEntity
import ru.netology.nework.utils.Converters

@Database(
    entities = [
        PostEntity::class,
        UserEntity::class,
        EventEntity::class,
        JobEntity::class
    ], version = 1
)

@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

    abstract fun eventDao(): EventDao

    abstract fun jobDao(): JobDao
}