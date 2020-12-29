package com.example.univstreet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class InfoActivity : FragmentActivity() {

    private val NUM_PAGES = 4

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    val restaurantIndexes = listOf("중국집", "치킨", "분식", "카페")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        back_img.setOnClickListener{
            onBackPressed()
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter


        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
               // Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_LONG).show()
                menu_text.text = restaurantIndexes[position]

            }
        }

        viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        var menu = intent.getStringExtra("menu");
        menu_text.text = menu

        var index = when (menu) {
            "중국집" -> 0
            "치킨" -> 1
            "분식" -> 2
            "카페" -> 3
            else -> 0
        }

        viewPager.currentItem = index

        TabLayoutMediator(tab_layout, viewPager) { tab, position ->
            tab.text = restaurantIndexes[position]
        }.attach()
    }


//    override fun onBackPressed() {
//        if (viewPager.currentItem == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed()
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.currentItem = viewPager.currentItem - 1
//        }
//    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ChinaFoodFragment()
                1 -> ChickenFragment()
                2 -> RamenFragment()
                else -> CafeFragment()
            }
        }
    }

}
