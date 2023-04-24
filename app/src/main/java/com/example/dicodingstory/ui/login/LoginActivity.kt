package com.example.dicodingstory.ui.login

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels {
            factory
        }
        loginViewModel.apply {
            getToken().observe(this@LoginActivity) {
                if (it != null) {
                    toHome()
                }
            }
        }
        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.edLoginEmail.text.toString().trim()
                val password = binding.edLoginPassword.text.toString().trim()
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

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        supportActionBar?.hide()
        animation()
    }

    private fun toHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        finish()
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