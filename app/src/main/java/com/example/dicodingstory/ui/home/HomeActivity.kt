package com.example.dicodingstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivityHomeBinding
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.utils.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String
    private val factory = ViewModelFactory.getInstance(this)
    private val homeViewModel: HomeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        homeViewModel.getToken().observe(this) {
            if (it != null) {
                token = it
                Log.d(TAG, token)
            }
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                showLoading(true)
                homeViewModel.clearToken()
                showLoading(false)
                toLogin()
                super.onOptionsItemSelected(item)
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun toLogin() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "Home"
    }
}