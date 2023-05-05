package com.example.dicodingstory.ui.register

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityRegisterBinding
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val registerViewModel: RegisterViewModel by viewModels {
            factory
        }
        setContentView(binding.root)
        binding.apply {
            btnRegister.setOnClickListener {
                if (edRegisterEmail.error.isNullOrEmpty() && edRegisterName.error.isNullOrEmpty() && edRegisterPassword.error.isNullOrEmpty()) {
                    val name = edRegisterName.text.toString().trim()
                    val email = edRegisterEmail.text.toString().trim()
                    val password = edRegisterPassword.text.toString().trim()
                    register(registerViewModel, name, email, password)
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        R.string.register_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            tvLogin.setOnClickListener {
                toLogin()
            }
        }
        animation()
    }

    private fun register(
        registerViewModel: RegisterViewModel,
        name: String,
        email: String,
        password: String
    ) {
        registerViewModel.saveUserRegister(name, email, password)
            .observe(this@RegisterActivity) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        toLogin()
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            R.string.register_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                }
            }
    }

    private fun toLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun animation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -75f, 75f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}