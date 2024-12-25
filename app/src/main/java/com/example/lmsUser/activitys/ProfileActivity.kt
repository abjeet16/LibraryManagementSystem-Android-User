package com.example.lmsUser.activitys

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lmsUser.DataModules.UserProfile
import com.example.lmsUser.R
import com.example.lmsUser.databinding.ActivityProfileBinding
import com.example.lmsUser.network.ApiClient

class ProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private var jwtToken: String? = null
    private val apiClient = ApiClient.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        jwtToken = sharedPreferences.getString("token", null)

        getProfileDetails()

    }

    private fun getProfileDetails() {
        jwtToken?.let {
            apiClient.getUserProfile(it,
                onSuccess = { profile ->
                    setUpDetails(profile)
                },
                onError = { error ->
                    // Handle error
                    Log.e("firstname", "Error: $error")
                }
            )
        }
    }

    private fun setUpDetails(profile: UserProfile) {
        binding.apply {
            main.visibility = View.VISIBLE
            firstNameEditText.setText(profile.firstName)
            lastNameEditText.setText(profile.lastName)
            emailEditText.setText(profile.username)
            authorityEditText.setText(profile.authorities[0])
            userIdEditText.setText(profile.userId.toString())
        }
    }
}