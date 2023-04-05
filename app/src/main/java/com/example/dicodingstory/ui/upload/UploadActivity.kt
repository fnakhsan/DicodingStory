package com.example.dicodingstory.ui.upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dicodingstory.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}