package com.example.dicodingstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel() {
    fun getToken(): LiveData<String?> = repository.getToken().asLiveData(Dispatchers.IO)

    fun getAllStories(token: String) = repository.getAllStories(token)

    fun clearToken() {
        viewModelScope.launch {
            repository.clearToken()
        }
    }
}