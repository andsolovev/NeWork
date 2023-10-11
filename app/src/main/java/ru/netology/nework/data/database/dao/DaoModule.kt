package ru.netology.nework.data.database.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.data.database.db.AppDb
@InstallIn(SingletonComponent::class)
@Module
class DaoModule {
    @Provides
    fun providePostDao(db: AppDb): PostDao = db.postDao()
    @Provides
    fun provideUserDao(db: AppDb): UserDao = db.userDao()

    @Provides
    fun provideEventDao(db: AppDb): EventDao = db.eventDao()

    @Provides
    fun provideJobDao(db: AppDb): JobDao = db.jobDao()

    @Provides
    fun provideWallDao(db: AppDb): WallDao = db.wallDao()
}