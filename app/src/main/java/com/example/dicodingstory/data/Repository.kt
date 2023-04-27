package com.example.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.dicodingstory.data.local.database.StoryDatabase
import com.example.dicodingstory.data.local.datastore.AuthDataStore
import com.example.dicodingstory.data.local.datastore.LocaleDataStore
import com.example.dicodingstory.data.model.*
import com.example.dicodingstory.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class Repository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
    private val authDataStore: AuthDataStore,
    private val localeDataStore: LocaleDataStore
) {
    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    fun getLocale(): Flow<String> = localeDataStore.getLocaleSetting()

    suspend fun saveLocale(locale: String) {
        localeDataStore.saveLocaleSetting(locale)
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

    fun getAllStories(token: String): LiveData<PagingData<StoryModel>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                apiService,
                storyDatabase,
                generateBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getAllStoriesLocation(token: String): LiveData<Result<ListStoryModel>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getAllStories(generateBearerToken(token), location = 1)
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

    fun uploadStory(
        token: String,
        description: String,
        image: MultipartBody.Part
    ): LiveData<Result<UploadModel>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(
                generateBearerToken(token),
                description.toRequestBody("text/plain".toMediaType()),
                image
            )
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
            storyDatabase: StoryDatabase,
            authDataStore: AuthDataStore,
            localeDataStore: LocaleDataStore
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, storyDatabase, authDataStore, localeDataStore)
        }.also { instance = it }
    }
}