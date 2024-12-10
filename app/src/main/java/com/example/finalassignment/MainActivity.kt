package edu.temple.browsr

import android.content.Context
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity(), TabFragment.ControlInterface{

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val recyclerView: RecyclerView? by lazy {
        findViewById(R.id.recyclerView)
    }

    private val tabLayout: TabLayout? by lazy {
        findViewById(R.id.tabLayout)
    }

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        restoreTabs()

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = browserViewModel.getNumberOfTabs()

            // Each TabFragment maintains an ID assigned at instantiation.
            // This is used to notify the parent that a specific tab wants to update its title
            override fun createFragment(position: Int) = TabFragment.newInstance(position)
        }


        // Only if present (portrait)
        tabLayout?.run{

            // Keeps ViewPager and TabLayout selection in sync automatically
            // lambda updates title
            TabLayoutMediator(this, viewPager) { tab, position ->
                tab.text = browserViewModel.getPage(position).title
            }.attach()
        }

        // Only if present (landscape)
        recyclerView?.run{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PageAdapter(browserViewModel.tabs){
                viewPager.setCurrentItem(it, true)
            }
        }

        // Observe titles and update TabLayout or RecyclerView
        browserViewModel.getUpdate().observe(this) {
            viewPager.adapter?.notifyItemChanged(it)
            recyclerView?.adapter?.notifyItemChanged(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        saveTabs()
    }

    // TabFragment.ControlInterface callback
    override fun newPage() {
        browserViewModel.addTab()
        viewPager.setCurrentItem(browserViewModel.getNumberOfTabs() - 1, true)
    }

    private fun saveTabs() {
        val fileOutputStream = openFileOutput("tabs.dat", Context.MODE_PRIVATE)
        ObjectOutputStream(fileOutputStream).use { output ->
            output.writeObject(browserViewModel)
        }
    }

    private fun restoreTabs() {
        try {
            val fileInputStream = openFileInput("tabs.dat")
            ObjectInputStream(fileInputStream).use { input ->
                val restoredViewModel = input.readObject() as BrowserViewModel
                browserViewModel.tabs.clear()
                browserViewModel.tabs.addAll(restoredViewModel.tabs)
            }
        } catch (e: Exception) {

        }
    }



}















