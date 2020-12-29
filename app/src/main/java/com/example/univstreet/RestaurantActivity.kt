package com.example.univstreet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.activity_restaurant.tab_layout

class RestaurantActivity : AppCompatActivity() {

    private val NUM_PAGES = 3

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    val options = listOf("메뉴", "정보", "리뷰")

    companion object{
        var id = ""
        var name = ""
        var address = ""
        var runtime = ""
        var phone = ""
        var seat  = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        val getId = intent.getStringExtra("id")
        val getName = intent.getStringExtra("name")
        val getAddress = intent.getStringExtra("address")
        val getRuntime = intent.getStringExtra("runtime")
        val getPhone = intent.getStringExtra("phone")
        val getSeat = intent.getIntExtra("seat", 0)

        id = getId
        name = getName
        address = getAddress
        runtime = getRuntime
        phone = getPhone
        seat = getSeat

        name_text.text = name

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter


        TabLayoutMediator(tab_layout, viewPager) { tab, position ->
            tab.text = options[position]
        }.attach()

    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MenuFragment()
                1 -> InfoFragment()
                else -> ReviewFragment()
            }
        }
    }









}
