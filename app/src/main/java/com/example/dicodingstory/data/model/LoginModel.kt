package com.example.dicodingstory.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: LoginRequest,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginRequest(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String
)
