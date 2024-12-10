package com.example.finalassignment

import android.content.Context
import edu.temple.browsr.Bookmark

class BookmarkManager (private val context: Context){

    private val prefs = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)

    fun addBookmark(bookmark: Bookmark): Boolean {
        val bookmarks = getBookmarks().toMutableList()
        if (bookmarks.any { it.url == bookmark.url }) {
            return false // URL already exists
        }
        bookmarks.add(bookmark)
        saveBookmarks(bookmarks)
        return true
    }

    fun getBookmarks(): List<Bookmark> {
        val bookmarkString = prefs.getString("bookmark_list", "") ?: return emptyList()
        return bookmarkString.split(";").mapNotNull {
            val parts = it.split(".")
            if (parts.size == 2) Bookmark(parts[0], parts[1]) else null
        }
    }

    fun deleteBookmark(bookmark: Bookmark) {
        val bookmarks = getBookmarks().toMutableList()
        bookmarks.remove(bookmark)
        saveBookmarks(bookmarks)
    }

    private fun saveBookmarks(bookmarks: List<Bookmark>) {
        val bookmarkString = bookmarks.joinToString(";") { "${it.title},${it.url}"}
        prefs.edit().putString("bookmark_list", bookmarkString).apply()
    }
}

















