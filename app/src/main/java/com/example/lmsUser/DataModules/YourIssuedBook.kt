package com.example.lmsUser.DataModules

import java.time.LocalDate

data class YourIssuedBook(
    val issueId:Long,
    val userId: Long,        // ID of the user borrowing the book
    val bookCopy: BookCopy,      // ID of the book copy being borrowed
    val issueDate: String, // Date when the book is issued
    val returnDate: String ,// Expected return date
    val actualReturnDate:String?
    )