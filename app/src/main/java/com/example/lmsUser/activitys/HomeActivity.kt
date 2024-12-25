package com.example.lmsUser.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lmsUser.databinding.ActivityHomeBinding
import com.example.lmsUser.network.ApiClient

class HomeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    val apiClient = ApiClient.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        logout()
        makeTestRequest()
    }
    private fun logout() {
        binding.logout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("token")
            editor.apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun makeTestRequest(){
        binding.userRequest.setOnClickListener {
            userRequest()
        }
        binding.adminRequest.setOnClickListener {
            adminRequest()
        }
    }
    private fun adminRequest() {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("token", null)
        if (jwtToken != null) {
            apiClient.fetchAuthenticatedData(
                endpoint = "admin", // Use "admin" for the other endpoint
                token = jwtToken,
                onSuccess = { response ->
                    Log.d("AuthenticatedRequest", "Response: $response")
                    Toast.makeText(this, "Welcome, ${response}!", Toast.LENGTH_LONG).show()
                    // Handle success
                },
                onError = { error ->
                    Log.e("AuthenticatedRequest", "Error: $error")
                    // Handle error
                }
            )
        }
    }

    private fun userRequest() {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("token", null)
        if (jwtToken != null) {
            apiClient.fetchAuthenticatedData(
                endpoint = "user", // Use "admin" for the other endpoint
                token = jwtToken,
                onSuccess = { response ->
                    Log.d("AuthenticatedRequest", "Response: $response")
                    Toast.makeText(this, "Welcome, ${response}!", Toast.LENGTH_LONG).show()
                    // Handle success
                },
                onError = { error ->
                    Log.e("AuthenticatedRequest", "Error: $error")
                    // Handle error
                }
            )
        }
    }
}