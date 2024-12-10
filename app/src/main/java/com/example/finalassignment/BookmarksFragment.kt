package com.example.finalassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.temple.browsr.BookmarkAdapter

class BookmarksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var buttonAddBookmark: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewBookmark)
        buttonAddBookmark = view.findViewById(R.id.buttonAddBookmark)

        recyclerView.layoutManager = LinearLayoutManager(context)
        bookmarkAdapter = BookmarkAdapter(mutableListOf())
        recyclerView.adapter = bookmarkAdapter

        buttonAddBookmark.setOnClickListener {
            Toast.makeText(context, "Add Bookmark clicked", Toast.LENGTH_SHORT).show()
        }

    }
}

























