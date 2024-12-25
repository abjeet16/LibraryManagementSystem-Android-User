package com.example.lmsUser.activitys

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lmsUser.DataModules.Book
import com.example.lmsUser.R
import com.example.lmsUser.activitys.adapter.AllBooksAdapter
import com.example.lmsUser.databinding.ActivityAllBooksBinding
import com.example.lmsUser.network.ApiClient

class AllBooksActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAllBooksBinding.inflate(layoutInflater)
    }

    private var jwtToken: String? = null
    private val apiClient = ApiClient.getInstance(this)

    private lateinit var allBooks: ArrayList<Book>
    private lateinit var filteredBooks: ArrayList<Book>
    private lateinit var adapter: AllBooksAdapter

    // Enum for search type
    enum class SearchBy {
        BOOK_NAME, AUTHOR_NAME, PUBLICATION_NAME
    }

    private var currentSearchBy: SearchBy = SearchBy.BOOK_NAME // Default search type


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        jwtToken = sharedPreferences.getString("token", null)

        fetchAllBooks()
        setupRecyclerView()
        setupDropdown()
        setupSearchBar()
    }
    private fun fetchAllBooks() {
        jwtToken?.let {
            apiClient.fetchBooks(
                token = it,
                onSuccess = { books ->
                    allBooks = ArrayList(books)
                    filteredBooks = ArrayList(books)
                    updateRecyclerView(filteredBooks)
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        } ?: run {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = AllBooksAdapter(arrayListOf())
        binding.recyclerview.adapter = adapter
    }

    private fun setupDropdown() {
        val options = arrayOf("Book Name", "Author Name", "Publication Name")
        val dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        binding.dropdownMenu.adapter = dropdownAdapter

        binding.dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentSearchBy = when (position) {
                    0 -> SearchBy.BOOK_NAME
                    1 -> SearchBy.AUTHOR_NAME
                    2 -> SearchBy.PUBLICATION_NAME
                    else -> SearchBy.BOOK_NAME
                }
                Log.d("SearchBy", "Current search by: $currentSearchBy")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                currentSearchBy = SearchBy.BOOK_NAME
            }
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed (optional, can be left empty)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Called as the text is being changed
                val query = charSequence.toString().trim()
                if (query.isNotEmpty()) {
                    filterBooks(query)  // Perform filtering when text changes
                } else {
                    resetFilter()  // Reset filter if text is empty
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // Called after the text has been changed (optional, can be left empty)
            }
        })
    }


    private fun resetFilter() {
        filteredBooks.clear()
        filteredBooks.addAll(allBooks)
        updateRecyclerView(filteredBooks)
    }

    private fun filterBooks(query: String) {
        val filteredList = when (currentSearchBy) {
            SearchBy.BOOK_NAME -> allBooks.filter { it.name.contains(query, ignoreCase = true) }
            SearchBy.AUTHOR_NAME -> allBooks.filter { it.author.contains(query, ignoreCase = true) }
            SearchBy.PUBLICATION_NAME -> allBooks.filter { it.publicationName.contains(query, ignoreCase = true) }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No results found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
        Log.d("filterListBooks", filteredList.toString())
        updateRecyclerView(ArrayList(filteredList))  // Ensure you're updating the RecyclerView with the filtered data
    }

    private fun updateRecyclerView(newBooks: List<Book>) {
        Log.d("updateRecyclerView", "Updating with new data: ${newBooks.size}")

        // First, update the adapter's data directly
        adapter.updateBooks(newBooks)  // If your adapter uses ListAdapter

        // You can still use DiffUtil here for optimization if needed:
        val diffCallback = BooksDiffCallback(filteredBooks, newBooks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        filteredBooks.clear()
        filteredBooks.addAll(newBooks)
        diffResult.dispatchUpdatesTo(adapter)
    }


    // DiffUtil Callback for efficient updates
    class BooksDiffCallback(
        private val oldList: List<Book>,
        private val newList: List<Book>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}