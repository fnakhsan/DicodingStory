package com.example.dicodingstory.data.network

import com.example.dicodingstory.data.model.DetailStoryResponse
import com.example.dicodingstory.data.model.ListStoryModel
import com.example.dicodingstory.data.model.LoginResponse
import com.example.dicodingstory.data.model.RegisterModel
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterModel

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): ListStoryModel

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse
}