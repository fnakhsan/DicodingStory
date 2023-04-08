package com.example.dicodingstory.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: Repository): ViewModel() {
    fun getLocale() = repository.getLocale().asLiveData(Dispatchers.IO)

    fun saveLocale(localeName: String) {
        viewModelScope.launch {
            repository.saveLocale(localeName)
        }
    }
}