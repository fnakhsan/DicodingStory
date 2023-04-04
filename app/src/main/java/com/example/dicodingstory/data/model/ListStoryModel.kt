package com.example.dicodingstory.data.model

import com.google.gson.annotations.SerializedName

data class ListStoryModel(
	@field:SerializedName("listStory")
	val listStory: List<StoryModel?>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
