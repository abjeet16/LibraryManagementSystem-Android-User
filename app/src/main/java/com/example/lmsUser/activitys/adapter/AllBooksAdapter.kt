package com.example.lmsUser.activitys.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lmsUser.DataModules.Book
import com.example.lmsUser.databinding.AllBooksViewHolderBinding

class AllBooksAdapter(private var books: ArrayList<Book>)
    : RecyclerView.Adapter<AllBooksAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: AllBooksViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind the book data directly to the views
        fun bind(book: Book) {
            binding.bookname.text = book.name
            binding.auther.text = book.author
            binding.volume.text = book.volume.toString()
            binding.year.text = book.publicationYear.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AllBooksViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pass the book object to bind
        holder.bind(books[position])
    }

    // Update data in the adapter
    fun updateBooks(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}