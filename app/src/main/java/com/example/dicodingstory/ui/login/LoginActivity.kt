package com.example.dicodingstory.ui.login

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.LocaleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityLoginBinding
import com.example.dicodingstory.ui.main.MainActivity
import com.example.dicodingstory.ui.register.RegisterActivity
import com.example.dicodingstory.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_DicodingStory)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels {
            factory
        }
        loginViewModel.getToken().observe(this@LoginActivity) {
            if (it != null) {
                toHome()
            } else {
                setLocale(loginViewModel)
            }
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnLogin.setOnClickListener {
                if (edLoginEmail.error.isNullOrEmpty() && edLoginPassword.error.isNullOrEmpty()) {
                    val email = edLoginEmail.text.toString().trim()
                    val password = edLoginPassword.text.toString().trim()
                    login(loginViewModel, email, password)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.login_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        supportActionBar?.hide()
        animation()
    }

    private fun login(loginViewModel: LoginViewModel, email: String, password: String) {
        loginViewModel.getUserLogin(email, password).observe(this@LoginActivity) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    toHome()
                }
                is Result.Error -> {
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.login_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun toHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        finish()
    }

    private fun setLocale(loginViewModel: LoginViewModel) {
        loginViewModel.getLocale().observe(this@LoginActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                baseContext.getSystemService(LocaleManager::class.java).applicationLocales =
                    LocaleList.forLanguageTags(it)
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(it))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun animation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, 0f, 150f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}