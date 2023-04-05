package com.example.dicodingstory.ui.register

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun saveUserRegister(name: String, email: String, password: String) =
        repository.saveUserRegister(name, email, password)
}