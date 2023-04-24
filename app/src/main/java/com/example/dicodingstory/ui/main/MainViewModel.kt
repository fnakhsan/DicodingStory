package com.example.dicodingstory.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun clearToken() {
        viewModelScope.launch {
            repository.clearToken()
        }
    }
}