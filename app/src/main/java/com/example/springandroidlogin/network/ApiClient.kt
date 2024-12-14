package com.example.springandroidlogin.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.springandroidlogin.DataModules.UserRegister
import com.example.springandroidlogin.DataModules.userLogin.UserLoginRequest
import com.example.springandroidlogin.DataModules.userLogin.UserLoginResponse
import com.example.springandroidlogin.helpers.ApiLinkHelper
import com.google.gson.Gson
import org.json.JSONObject

class ApiClient private constructor(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)
    private val apiLinkHelper = ApiLinkHelper()

    companion object {
        @Volatile
        private var INSTANCE: ApiClient? = null

        fun getInstance(context: Context): ApiClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiClient(context).also { INSTANCE = it }
            }
        }
    }

    fun registerUser(
        user: UserRegister,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val stringRequest = object : StringRequest(Method.POST,apiLinkHelper.registerUserApiUri(),
            { response ->
                Log.e("ApiClient", response)
                // If successful, invoke the onSuccess callback with the response message
                onSuccess(response)
            },
            { error ->
                // Check if the network response is not null
                if (error.networkResponse != null) {
                    // Convert the byte array to a string
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error during registration: $errorResponse")
                    // Handle error and pass the error message to onError
                    onError(errorResponse)
                } else {
                    // If there is no network response, print the general error message
                    Log.e("ApiClient", "Error during registration: ${error.message}")
                    onError(error.message ?: "An error occurred")
                }
            }
        ) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "first_name" to user.firstName,
                    "last_name" to user.lastName,
                    "email" to user.email,
                    "password" to user.password,
                    "phone_number" to user.phone_number
                )
            }
        }

        requestQueue.add(stringRequest)
    }
    //END OF REGISTER USER

    fun loginUser(
        user: UserLoginRequest,
        onSuccess: (UserLoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Create a JSON object for the request body
        val jsonBody = JSONObject().apply {
            put("email", user.email)
            put("password", user.password)
        }

        // Create a JsonObjectRequest
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, apiLinkHelper.loginUserApiUri(), jsonBody,
            { response ->
                Log.e("ApiClient", response.toString())
                try {
                    // Parse the JSON response using Gson
                    val loginResponse = Gson().fromJson(response.toString(), UserLoginResponse::class.java)
                    onSuccess(loginResponse)
                } catch (e: Exception) {
                    Log.e("ApiClient", "Error parsing login response: ${e.message}")
                    onError("Error parsing response")
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorResponse = String(error.networkResponse.data)

                    Log.e("ApiClient", "Status Code: $statusCode")
                    Log.e("ApiClient", "Error Response: $errorResponse")

                    onError("Error $statusCode: $errorResponse")
                } else {
                    Log.e("ApiClient", "Error during login: ${error.message}")
                    onError(error.message ?: "Unknown network error occurred")
                }
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest)
    }
    //END OF LOGIN

    fun isTokenExpired(
        token: String,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val stringRequest = object : StringRequest(
            Method.POST,
            apiLinkHelper.isTokenExpiredApiUri(),
            { response ->
                try {
                    // Parse the response (expected to be a boolean)
                    val isExpired = response.toBoolean()
                    Log.e("ApiClient", "Token is expired: $isExpired")
                    onSuccess(isExpired)
                } catch (e: Exception) {
                    Log.e("ApiClient", "Error parsing token expiry response: ${e.message}")
                    onError("Failed to parse the server response.")
                }
            },
            { error ->
                // Handle error response
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error during token expiry check: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Error during token expiry check: ${error.message}")
                    onError(error.message ?: "An unknown error occurred.")
                }
            }
        ) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "token" to token
                )
            }
        }

        // Add the request to the queue
        requestQueue.add(stringRequest)
    }
    //END OF IS TOKEN EXPIRED

    fun fetchAuthenticatedData(
        token: String,
        endpoint : String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val stringRequest = object : StringRequest(
            Method.GET, if (endpoint=="admin"){apiLinkHelper.AdminTestApiUri()}else{ apiLinkHelper.UserTestApiUri()},
            { response ->
                Log.e("ApiClient", "Authenticated Response: $response")
                onSuccess(response)
            },
            { error ->
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf("Authorization" to "Bearer $token")
            }
        }

        requestQueue.add(stringRequest)
    }
    //END OF TEST
}
