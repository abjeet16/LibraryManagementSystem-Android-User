package com.example.lmsUser.activitys.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lmsUser.DataModules.YourIssuedBook
import com.example.lmsUser.databinding.AllBooksViewHolderBinding
import com.example.lmsUser.databinding.ItemBookIssuesBinding

class IssuedBookAdapter(private val books: List<YourIssuedBook>) : RecyclerView.Adapter<IssuedBookAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemBookIssuesBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind(yourIssuedBook: YourIssuedBook) {
            binding.apply {
                if (yourIssuedBook.actualReturnDate == null) {
                    actualReturnDate.visibility = View.GONE
                }
                issueId.text = "Issue ID: ${String.format(yourIssuedBook.issueId.toString())}"
                bookName.text = "Book Name: ${yourIssuedBook.bookCopy.book.name}"
                issueDate.text = "Issue Date: ${yourIssuedBook.issueDate}"
                returnDate.text = "Return Date: ${yourIssuedBook.returnDate}"
                actualReturnDate.text = "Actual Return Date: ${yourIssuedBook.actualReturnDate}"
                authorName.text = "Author: ${yourIssuedBook.bookCopy.book.author}"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookIssuesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }
}