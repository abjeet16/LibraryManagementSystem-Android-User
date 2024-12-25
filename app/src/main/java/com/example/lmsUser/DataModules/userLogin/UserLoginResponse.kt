package com.example.lmsUser.DataModules.userLogin


import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("username")
    val username: String?
)