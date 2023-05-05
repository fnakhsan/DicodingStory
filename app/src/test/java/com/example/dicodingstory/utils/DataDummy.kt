package com.example.dicodingstory.utils

import com.example.dicodingstory.data.model.StoryModel

object DataDummy {
    fun generateDummyListStory(): List<StoryModel> {
        val storyItems: MutableList<StoryModel> = arrayListOf()

        for (i in 0..10) {
            val story = StoryModel(
                id = "story-NyGJQv7oYd59DC-U",
                name = "Fatkhi Nur Akhsan",
                description = "Agus",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1683035377773_BPKaDWeA.jpg",
                createdAt = "2023-05-02T13:49:37.774Z",
                lat = -7.7794882,
                lon = 110.3909943
            )
            storyItems.add(story)
        }

        return storyItems
    }
}