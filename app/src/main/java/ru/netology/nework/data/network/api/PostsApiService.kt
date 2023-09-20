package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.netology.nework.domain.models.Post

interface PostsApiService {

    @GET("posts")
    suspend fun getPosts() : Response<List<Post>>

    @GET("posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int) : Response<List<Post>>
}