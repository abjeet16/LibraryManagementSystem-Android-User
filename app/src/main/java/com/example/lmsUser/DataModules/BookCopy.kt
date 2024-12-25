package com.example.lmsUser.DataModules

data class BookCopy(
    val copyId: Int,
    val book: Book,
    val isbn: String,
    val status: String,
    val location: String
)
