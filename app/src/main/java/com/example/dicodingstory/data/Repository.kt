package com.example.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingstory.data.local.AuthDataStore
import com.example.dicodingstory.data.model.LoginResponse
import com.example.dicodingstory.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class Repository(private val apiService: ApiService, private val authDataStore: AuthDataStore) {

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

    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
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