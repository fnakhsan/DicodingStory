package com.example.dicodingstory.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingstory.data.Repository
import com.example.dicodingstory.di.Injection
import com.example.dicodingstory.ui.detail.DetailViewModel
import com.example.dicodingstory.ui.home.HomeViewModel
import com.example.dicodingstory.ui.login.LoginViewModel
import com.example.dicodingstory.ui.register.RegisterViewModel
import com.example.dicodingstory.ui.setting.SettingViewModel
import com.example.dicodingstory.ui.upload.UploadViewModel

class ViewModelFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> return LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> return RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(repository) as T
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> return UploadViewModel(repository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> return SettingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}