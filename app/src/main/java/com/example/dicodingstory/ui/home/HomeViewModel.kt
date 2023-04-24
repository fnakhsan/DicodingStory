package com.example.dicodingstory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val repository: Repository): ViewModel() {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getAllStories(token: String) = repository.getAllStories(token)
}