package com.example.lmsUser.activitys

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lmsUser.R
import com.example.lmsUser.databinding.ActivityIssuedBookBinding
import com.example.lmsUser.network.ApiClient

class IssuedBookActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityIssuedBookBinding.inflate(layoutInflater)
    }

    private var jwtToken: String? = null
    private val apiClient = ApiClient.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        jwtToken = sharedPreferences.getString("token", null)

        fetchIssuedBooks()
    }

    private fun fetchIssuedBooks() {
        jwtToken?.let {
            apiClient.yourIssuedBooks(
                token = it,
                onSuccess = { books ->
                    Log.d("my issues", books.toString())
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        } ?: run {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
        }
    }
}