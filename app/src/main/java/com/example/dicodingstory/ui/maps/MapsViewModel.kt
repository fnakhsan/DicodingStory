package com.example.dicodingstory.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.Dispatchers

class MapsViewModel(private val repository: Repository): ViewModel() {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getAllStoriesLocation(token: String) = repository.getAllStoriesLocation(token)
}