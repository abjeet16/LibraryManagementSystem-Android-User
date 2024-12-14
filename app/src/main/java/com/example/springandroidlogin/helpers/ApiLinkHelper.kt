package com.example.springandroidlogin.helpers

class ApiLinkHelper {
    // MAIN BASE API URI:
    val BASE_URL: String = "http://192.168.29.30:8081/api/v1/"

    fun loginUserApiUri(): String {
        return BASE_URL + "user/auth/login"
    }
    // END OF AUTHENTICATE USER API URI METHOD.
    fun registerUserApiUri(): String {
        return BASE_URL + "user/auth/register"
    }
    // END OF REGISTER USER API URI METHOD.
    fun isTokenExpiredApiUri(): String {
        return BASE_URL + "auth/is_token_expired"
    }
    fun AdminTestApiUri(): String {
        return BASE_URL + "test/admin"
    }
    fun UserTestApiUri(): String {
        return BASE_URL + "test/user"
    }
    // END OF IS TOKEN EXPIRED API URI METHOD.
}