package com.example.springandroidlogin.activitys.Auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.springandroidlogin.DataModules.userLogin.UserLoginRequest
import com.example.springandroidlogin.DataModules.userLogin.UserLoginResponse
import com.example.springandroidlogin.activitys.MainActivity
import com.example.springandroidlogin.databinding.ActivitySignInBinding
import com.example.springandroidlogin.network.ApiClient

class SignIn : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        // Validate input fields
        if (binding.email.text.isEmpty()) {
            Toast.makeText(this, "Email Required", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.password.text.isEmpty()) {
            Toast.makeText(this, "Password Required", Toast.LENGTH_LONG).show()
            return
        }

        // Create UserLoginRequest object
        val user = UserLoginRequest(binding.email.text.toString(), binding.password.text.toString())

        // Call the API to log in the user
        ApiClient.getInstance(this).loginUser(
            user = user,
            onSuccess = { response ->
                // Save user session
                saveUserSession(response)

                // Welcome the user
                Toast.makeText(this, "Welcome, ${response.firstName}!", Toast.LENGTH_LONG).show()

                // Navigate to the Home activity
                goToHome()
            },
            onError = { error ->
                // Handle login error
                Toast.makeText(this, "Login failed: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to login
    }

    fun goToSignUpAct(view: View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

    private fun saveUserSession(response: UserLoginResponse) {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save user details
        editor.putString("token", response.token)
        editor.putInt("userId", response.userId ?: 0) // Provide a default value of 0 for userId
        editor.putString("username", response.username)
        editor.putString("firstName", response.firstName ?: "") // Default to empty string
        editor.putString("lastName", response.lastName ?: "")   // Default to empty string

        // Apply changes
        editor.apply()
    }
}
