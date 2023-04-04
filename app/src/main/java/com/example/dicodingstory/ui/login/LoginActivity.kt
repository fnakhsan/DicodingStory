package com.example.dicodingstory.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityLoginBinding
import com.example.dicodingstory.ui.home.HomeActivity
import com.example.dicodingstory.ui.register.RegisterActivity
import com.example.dicodingstory.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels {
            factory
        }
        loginViewModel.getToken().observe(this) {
            if (it != null) {
                toHome()
            }
        }
        setContentView(binding.root)
        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
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
    }

    private fun toHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//        intent.putExtra(EXTRA_TOKEN, token)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        //        const val EXTRA_TOKEN = "extra_token"
        private const val TAG = "login"
    }
}