package com.example.lmsUser.DataModules

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val userId: Long,
    val username: String, // Email
    val authorities: List<String> // List of authorities (roles)
)
