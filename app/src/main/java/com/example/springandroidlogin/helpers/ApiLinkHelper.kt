package com.example.springandroidlogin.helpers

class ApiLinkHelper {
    // MAIN BASE API URI:
    private val BASE_URL: String = "http://192.168.29.30:8081/api/v1/"

    fun loginUserApiUri(): String {
        return BASE_URL + "auth/login"
    }
    // END OF AUTHENTICATE USER API URI METHOD.
    fun registerUserApiUri(): String {
        return BASE_URL + "auth/register"
    }
    // END OF REGISTER USER API URI METHOD.
    fun getMyNotesApiUri(): String {
        return BASE_URL + "note/my_notes"
    }
    // END OF GET MY NOTES API URI METHOD.
    fun createNoteApiUri(): String {
        return BASE_URL + "note/create"
    }
    // END OF CREATE NOTES API URI METHOD.
    fun deleteNoteApiUri(): String {
        return BASE_URL + "note/delete"
    }
    // END OF DELETE NOTE API URI METHOD.
    fun isTokenExpiredApiUri(): String {
        return BASE_URL + "auth/is_token_expired"
    }
    // END OF IS TOKEN EXPIRED API URI METHOD.

}