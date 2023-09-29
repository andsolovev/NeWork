package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.netology.nework.domain.models.Post

interface PostsApiService {

    @GET("posts")
    suspend fun getPosts() : Response<List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post) : Response<Post>


}