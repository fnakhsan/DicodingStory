package com.example.dicodingstory.data.model

import com.google.gson.annotations.SerializedName

data class UploadModel(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)