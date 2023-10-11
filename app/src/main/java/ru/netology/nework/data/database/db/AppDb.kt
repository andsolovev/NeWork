package ru.netology.nework.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nework.data.database.dao.EventDao
import ru.netology.nework.data.database.dao.JobDao
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.dao.PostRemoteKeyDao
import ru.netology.nework.data.database.dao.UserDao
import ru.netology.nework.data.database.dao.WallDao
import ru.netology.nework.data.database.entity.EventEntity
import ru.netology.nework.data.database.entity.JobEntity
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.PostRemoteKeyEntity
import ru.netology.nework.data.database.entity.PostWallEntity
import ru.netology.nework.data.database.entity.UserEntity
import ru.netology.nework.utils.Converters

@Database(
    entities = [
        PostEntity::class,
        UserEntity::class,
        EventEntity::class,
        JobEntity::class,
        PostWallEntity::class,
        PostRemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

    abstract fun eventDao(): EventDao

    abstract fun jobDao(): JobDao

    abstract fun wallDao(): WallDao

    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}