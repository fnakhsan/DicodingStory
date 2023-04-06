package com.example.dicodingstory.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstory.data.Repository
import com.example.dicodingstory.data.local.AuthDataStore
import com.example.dicodingstory.data.local.LocaleDataStore
import com.example.dicodingstory.data.network.ApiConfig

private val Context.dataStore by preferencesDataStore("preferences")

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(
            apiService,
            AuthDataStore.getInstance(context.dataStore),
            LocaleDataStore.getInstance(context.dataStore)
        )
    }
}