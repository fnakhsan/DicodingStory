package com.example.dicodingstory.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dicodingstory.databinding.ActivityHomeBinding
import com.example.dicodingstory.ui.login.LoginActivity.Companion.EXTRA_TOKEN

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        Log.d(TAG, token)
    }

    companion object {
        private const val TAG = "Home"
    }
}