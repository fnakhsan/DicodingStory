package com.example.dicodingstory.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstory.data.AuthRepository
import com.example.dicodingstory.data.local.AuthDataStore
import com.example.dicodingstory.data.network.ApiConfig

private val Context.dataStore by preferencesDataStore("preferences")
object Injection {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService, AuthDataStore.getInstance(context.dataStore))
    }
}