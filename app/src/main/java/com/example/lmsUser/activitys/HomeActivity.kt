package com.example.lmsUser.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lmsUser.databinding.ActivityHomeBinding
import com.example.lmsUser.network.ApiClient

class HomeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var jwtToken: String? = null
    private val apiClient = ApiClient.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        jwtToken = sharedPreferences.getString("token", null)

        logout()
        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.apply {
            viewProfileImage.setOnClickListener {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            viewAllBooksImage.setOnClickListener {
                startActivity(Intent(this@HomeActivity, AllBooksActivity::class.java))
            }
            viewIssuedBooksImage.setOnClickListener {
                startActivity(Intent(this@HomeActivity, IssuedBookActivity::class.java))
            }
        }
    }

    private fun logout() {
        binding.logoutImage.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("token")
            editor.apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}