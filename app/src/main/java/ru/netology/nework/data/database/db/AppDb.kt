package ru.netology.nework.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nework.data.database.dao.Converters
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.dao.UserDao
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.UserEntity

@Database(entities = [PostEntity::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao() : PostDao
    abstract fun userDao() : UserDao
}