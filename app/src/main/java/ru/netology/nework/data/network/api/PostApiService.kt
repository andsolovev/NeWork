package ru.netology.nework.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nework.domain.model.Post

interface PostApiService {

    @GET("posts")
    suspend fun getPosts() : Response<List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post) : Response<Post>

    @DELETE("posts/{post_id}")
    suspend fun removePostById(@Path("post_id") id: Int): Response<Unit>

    @POST("posts/{post_id}/likes")
    suspend fun likePostById(@Path("post_id") id: Int): Response<Post>

    @DELETE("posts/{post_id}/likes")
    suspend fun unlikePostById(@Path("post_id") id: Int): Response<Post>


}