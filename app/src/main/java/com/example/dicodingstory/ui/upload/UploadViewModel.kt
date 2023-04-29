package com.example.dicodingstory.ui.upload

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstory.data.Repository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun uploadStory(token: String, description: String, image: MultipartBody.Part, location: Location?) =
        repository.uploadStory(token, description, image, location)
}