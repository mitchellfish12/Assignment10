package edu.temple.browsr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassignment.BookmarkManager
import com.example.finalassignment.R.*

class BookmarkActivity : AppCompatActivity() {
    private lateinit var bookmarkManager: BookmarkManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_bookmark)

        bookmarkManager = BookmarkManager(this)
        recyclerView = findViewById(id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        updateBookmarkList()
    }

    private fun updateBookmarkList() {
        val bookmarks = bookmarkManager.getBookmarks()
        val adapter = BookmarkAdapter(bookmarks, { bookmark ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("url", bookmark.url)
            startActivity(intent)
            finish()
        }, { bookmark ->
            showDeleteConfirmationDialog(bookmark)
        })
        recyclerView.adapter = adapter
    }

    private fun showDeleteConfirmationDialog(bookmark: Bookmark) {
        AlertDialog.Builder(this)
            .setTitle("Delete Bookmark")
            .setMessage("Are you sure you want to delete this bookmark?")
            .setPositiveButton("Yes") { _, _ ->
                bookmarkManager.deleteBookmark(bookmark)
                updateBookmarkList() // Refresh the list
            }
            .setNegativeButton("No", null)
            .show()
    }
}




















