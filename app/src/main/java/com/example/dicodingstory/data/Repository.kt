package com.example.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingstory.data.local.AuthDataStore
import com.example.dicodingstory.data.model.*
import com.example.dicodingstory.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class Repository(private val apiService: ApiService, private val authDataStore: AuthDataStore) {

    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    fun getUserLogin(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                saveToken(response.loginResult.token)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun saveUserRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterModel>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStories(token: String): LiveData<Result<ListStoryModel>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories(generateBearerToken(token))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailStory(token: String, id: String): LiveData<Result<DetailStoryResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getDetailStory(generateBearerToken(token), id)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadStory(token: String, description: String, image: MultipartBody.Part): LiveData<Result<UploadModel>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(generateBearerToken(token), description, image)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    private fun generateBearerToken(token: String): String {
        return if (token.contains("bearer", true)) {
            token
        } else {
            "Bearer $token"
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            authDataStore: AuthDataStore
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, authDataStore)
        }.also { instance = it }
    }
}