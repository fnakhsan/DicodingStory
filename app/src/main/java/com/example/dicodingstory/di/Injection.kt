package com.example.dicodingstory.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dicodingstory.data.Repository
import com.example.dicodingstory.data.local.database.StoryDatabase
import com.example.dicodingstory.data.local.datastore.AuthDataStore
import com.example.dicodingstory.data.local.datastore.LocaleDataStore
import com.example.dicodingstory.data.network.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(
            apiService,
            StoryDatabase.getDatabase(context),
            AuthDataStore.getInstance(context.dataStore),
            LocaleDataStore.getInstance(context.dataStore)
        )
    }
}