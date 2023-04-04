package com.example.dicodingstory.data.model

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)

data class Story(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Float?,

    @field:SerializedName("lon")
    val lon: Float?
)
