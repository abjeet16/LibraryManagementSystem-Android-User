package com.example.springandroidlogin.activitys.Auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.springandroidlogin.DataModules.UserRegister
import com.example.springandroidlogin.databinding.ActivitySignUpBinding
import com.example.springandroidlogin.helpers.StringHelper
import com.example.springandroidlogin.network.ApiClient

class SignUp : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            processFormFields()
        }
    }

    fun processFormFields() {
        if (!validateAllEditText()) {
            return
        }

        val user = UserRegister(
            binding.firstName.text.toString().trim(),
            binding.lastName.text.toString().trim(),
            binding.email.text.toString().trim(),
            binding.password.text.toString().trim()
        )

        ApiClient.getInstance(this).registerUser(user,
            { response ->
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                clearFormFields()
            },
            { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun clearFormFields() {
        binding.firstName.text = null
        binding.lastName.text = null
        binding.email.text = null
        binding.password.text = null
        binding.confirm.text = null
    }

    fun validateAllEditText(): Boolean {
        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.confirm.text.toString().trim()

        if (firstName.isEmpty()) {
            binding.firstName.error = "First Name Required"
            return false
        }
        if (lastName.isEmpty()) {
            binding.lastName.error = "Last Name Required"
            return false
        }

        if (email.isEmpty()) {
            binding.email.error = "Email Required"
            return false
        } else if (!StringHelper().regexEmailValidationPattern(email)) {
            binding.email.error = "Invalid Email"
            return false
        }

        if (password.isEmpty()) {
            binding.password.error = "Password Required"
            return false
        } else {
            val error = StringHelper().validatePassword(password)
            if (error != null) {
                binding.password.error = error
                return false
            }
        }

        if (confirmPassword.isEmpty()) {
            binding.confirm.error = "Confirm Password Required"
            return false
        } else if (password != confirmPassword) {
            binding.confirm.error = "Passwords do not match"
            return false
        }

        return true
    }
}
