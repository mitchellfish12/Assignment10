package edu.temple.browsr

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalassignment.BookmarkManager
import android.os.Build.VERSION_CODES.R


class PageFragment : Fragment() {

    private lateinit var pageInterface: PageInterface
    private lateinit var bookmarkManager: BookmarkManager
    private lateinit var webView : WebView
    private lateinit var prefs: SharedPreferences

    private val pageDataViewModel: PageDataViewModel by lazy {
        ViewModelProvider(requireParentFragment())[PageDataViewModel::class.java]
    }

    // Fetch ViewModel instance scoped against parent fragment
    // This gives new instance for each tab. i.e. tabs are not sharing the currentURL LiveData,
    // each instance of TabFragment will have its own

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pageInterface = requireParentFragment() as PageInterface
        prefs = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_page, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (webView) {
            settings.javaScriptEnabled = true

            // Allows knowledge of WebView's behavior
            webViewClient = object: WebViewClient() {
                // Be notified of the URL of a loaded page, regardless of why it was loaded
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // Store current page URL in viewmodel
                    url?.run {
                        // Don't attempt to access ViewModel if not attached to parent
                        host?.let {
                            pageDataViewModel.setCurrentUrl(this)
                            view?.title?.run {
                                pageInterface.updatePage(Page(this, url))
                            }
                        }
                    }

                }
            }

            // Retrieve and restore WebView state, including current page and history
            savedInstanceState?.run{
                restoreState(this)
            }
            bookmarkManager = BookmarkManager(requireContext())
            view.findViewById<Button>(R.id.addBookmark).setOnClickListener {
                val currentUrl = webView.url ?: return@setOnClickListener
                val currentTitle = webView.title ?: "_blank"
                if (bookmarkManager.addBookmark(Bookmark(currentTitle, currentUrl))) {
                    Toast.makeText(requireContext(), "Bookmark added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Bookmark already exists", Toast.LENGTH_SHORT).show()
                }
            }
            view.findViewById<Button>(R.id.shareBookmark).setOnClickListener {
                webView.url?.let { url ->

                }
            }
        }
    }

    fun loadUrl(url: String) {
        if (url.isNotEmpty())
            webView.loadUrl(fixUrl(url))
    }

    fun goNext() {
        webView.goForward()
    }

    fun goBack() {
        webView.goBack()
    }

    // Process URL to fix specified malformed URLs or perform a search
    private fun fixUrl(url: String) : String {
        return if ((url.startsWith("https://")
                    || url.startsWith("http://"))
            && url.contains(Regex("[A-Za-z]+\\.[A-Za-z]+")))
            url
        else if (!(url.startsWith("https://")
                    || url.startsWith("http://"))
            && url.contains(Regex("[A-Za-z]+\\.[A-Za-z]+")))
            "https://$url"
        else
            searchUrl(url)
    }

    // Store WebView state, including current page and history
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    private fun searchUrl (query: String) = "https://duckduckgo.com/?q=$query"

    interface PageInterface {
        fun updatePage(page: Page)
    }

}























