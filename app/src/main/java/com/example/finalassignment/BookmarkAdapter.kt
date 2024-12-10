package edu.temple.browsr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassignment.R

class BookmarkAdapter(private val bookmarks: List<Bookmark>, private val onClick: (Bookmark) -> Unit, private val onDelete: (Bookmark) -> Unit) :
    RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
        init {
            view.setOnClickListener { onClick(bookmarks[adapterPosition])}
            view.findViewById<View>(R.id.buttonDelete).setOnClickListener {
                onDelete(bookmarks[adapterPosition])
            }
        }

        fun bind(bookmark: Bookmark) {
            titleTextView.text = bookmark.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarks[position])
    }

    override fun getItemCount() = bookmarks.size
}























