package com.example.dicodingstory.data.network

import com.example.dicodingstory.data.model.ListStoryModel
import com.example.dicodingstory.data.model.LoginResponse
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): ListStoryModel
}