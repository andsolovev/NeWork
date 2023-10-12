package ru.netology.nework.data.network.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nework.BuildConfig
import ru.netology.nework.BuildConfig.BASE_URL
import ru.netology.nework.data.network.authentification.AppAuth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        logging: HttpLoggingInterceptor,
        appAuth: AppAuth
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = appAuth.state.value.token?.let { token ->
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", token)
                    .build()
            } ?: chain.request()
            chain.proceed(request)
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providePostApiService(
        retrofit: Retrofit
    ): PostApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideUserApiService(
        retrofit: Retrofit
    ): UserApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideAuthApiService(
        retrofit: Retrofit
    ): AuthApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideEventApiService(
        retrofit: Retrofit
    ): EventApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideJobApiService(
        retrofit: Retrofit
    ): JobApiService = retrofit.create()
}