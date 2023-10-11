package ru.netology.nework.data.database.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nework.data.database.dao.PostDao
import ru.netology.nework.data.database.dao.PostRemoteKeyDao
import ru.netology.nework.data.database.dao.WallDao
import ru.netology.nework.data.database.db.AppDb
import ru.netology.nework.data.database.entity.PostEntity
import ru.netology.nework.data.database.entity.toDto
import ru.netology.nework.data.database.entity.toEntity
import ru.netology.nework.data.database.entity.toPostWallEntity
import ru.netology.nework.data.network.api.PostApiService
import ru.netology.nework.domain.model.Post
import ru.netology.nework.domain.repository.PostRepository
import ru.netology.nework.utils.ApiException
import ru.netology.nework.utils.NetworkException
import ru.netology.nework.utils.UnknownException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val wallDao: WallDao,
    private val apiService: PostApiService,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb,
) : PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { postDao.getPagingSource() },
        remoteMediator = PostRemoteMediator(apiService, postDao, postRemoteKeyDao, appDb)
    ).flow
        .map {
            it.map {
                it.toDto()
            }
        }

//    override val wall = wallDao.getAll()
//        .map { it.toDto() }
//        .flowOn(Dispatchers.Default)

    override val wall = wallDao.getAll()
        .map { it.toDto() }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = apiService.getPosts()
            if(!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw Exception("Body is null")
            postDao.insert(body.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getNewerPosts() {
        try {
            val postId = postDao.max() ?: 0
            val response = apiService.getNewer(postId)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun getPostsWall(userId: Int) {
        wallDao.removeAll()
        try {
            val response = apiService.getPostsWall(userId)
            if(!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            wallDao.removeAll()
            wallDao.insert(body.toPostWallEntity())
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun savePost(post: Post) {
        try {
            val response = apiService.savePost(post)
            if(!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removePostById(id: Int) {
        try {
            val response = apiService.removePostById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            postDao.removePostById(id)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun likePostById(id: Int) {
        try {
            val response = apiService.likePostById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun unlikePostById(id: Int) {
        try {
            val response = apiService.unlikePostById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}