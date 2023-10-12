package ru.netology.nework.data.database.repository

import androidx.paging.*
import androidx.room.withTransaction
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.dao.PostRemoteKeyDao
import ru.netology.nework.data.database.db.AppDb
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.PostRemoteKeyEntity
import ru.netology.nework.data.network.api.PostApiService
import ru.netology.nework.utils.ApiException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val service: PostApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    val id = postRemoteKeyDao.max()
                    if (id == null) {
                        service.getLatest(state.config.pageSize)
                    } else service.getAfter(id, state.config.pageSize)
                }

                LoadType.PREPEND -> return MediatorResult.Success(true)

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(
                response.code(),
                response.message(),
            )

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id
                                )
                            )
                        )
                    }

                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    body.last().id
                                )
                            )
                        )
                    }

                    else -> {}
                }
            }

            postDao.insert(body.map { PostEntity.fromDto(it) })

            return MediatorResult.Success(body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}