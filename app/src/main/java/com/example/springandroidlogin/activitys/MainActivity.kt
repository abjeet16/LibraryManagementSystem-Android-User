package com.example.springandroidlogin.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.springandroidlogin.activitys.Auth.SignIn
import com.example.springandroidlogin.activitys.Auth.SignUp
import com.example.springandroidlogin.databinding.ActivityMainBinding
import com.example.springandroidlogin.network.ApiClient

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Check if a session exists
        if (checkForSession()) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Set up click listeners for navigation buttons
        binding.signIn.setOnClickListener {
            navigateToSignIn()
        }

        binding.signUp.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }
    private fun checkForSession(): Boolean {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Retrieve the token from SharedPreferences
        val token = sharedPreferences.getString("token",null)
        val firstName = sharedPreferences.getString("firstName","please login or sign up")

        // If no token is found, return false (no session exists)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "No session found. Please log in.", Toast.LENGTH_LONG).show()
            return false
        }

        // Check if the token has expired by calling the API
        var isSessionValid = true // Default to true, will be updated by API response
        ApiClient.getInstance(this).isTokenExpired(
            token = token,
            onSuccess = { isExpired ->
                if (isExpired) {
                    Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
                    isSessionValid = false
                }
            },
            onError = { error ->
                Toast.makeText(this, "Failed to check session: $error", Toast.LENGTH_LONG).show()
                isSessionValid = false
            }
        )
        // Debugging: Display user name (if exists)
        if (!firstName.isNullOrEmpty()&&isSessionValid) {
            Toast.makeText(this, "Welcome back, $firstName!", Toast.LENGTH_LONG).show()
        }
        // Return the session validity status
        return isSessionValid
    }
}

