package com.example.dicodingstory.ui.login

import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getUserLogin(email: String, password: String) =
        authRepository.getUserLogin(email, password)
}