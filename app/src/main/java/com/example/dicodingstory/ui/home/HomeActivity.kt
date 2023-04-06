package com.example.dicodingstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.data.model.StoryModel
import com.example.dicodingstory.databinding.ActivityHomeBinding
import com.example.dicodingstory.ui.detail.DetailActivity
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.ui.setting.SettingActivity
import com.example.dicodingstory.ui.upload.UploadActivity
import com.example.dicodingstory.utils.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val homeViewModel: HomeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.addItemDecoration(itemDecoration)
            fabUpload.setOnClickListener {
                val intent = Intent(this@HomeActivity, UploadActivity::class.java)
                startActivity(intent)
            }
        }

        homeViewModel.getToken().observe(this) { token ->
            if (token != null) {
                Log.d(TAG, token)
                homeViewModel.getAllStories(token).observe(this){
                    when(it) {
                        is Result.Success -> {
                            setListStories(it.data.listStory)
                            showLoading(false)
                        }
                        is Result.Error -> {
                            showLoading(false)
                        }
                        is Result.Loading -> {
                            showLoading(true)
                        }
                    }
                }
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
            R.id.action_logout -> {
                showLoading(true)
                homeViewModel.clearToken()
                showLoading(false)
                toLogin()
                super.onOptionsItemSelected(item)
            }
            R.id.action_setting -> {
                val intent = Intent(this@HomeActivity, SettingActivity::class.java)
                startActivity(intent)
                super.onOptionsItemSelected(item)
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setListStories(data: List<StoryModel?>) {
        val adapter = HomeAdapter(data)
        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryModel, optionsCompat: ActivityOptionsCompat) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_ID, data.id)
                startActivity(intent, optionsCompat.toBundle())
            }
        })
        binding.rvStory.adapter = adapter
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
        const val EXTRA_ID = "extra_id"
    }
}