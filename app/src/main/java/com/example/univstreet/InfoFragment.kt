package com.example.univstreet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.info_fragment_layout.*

class InfoFragment : Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.info_fragment_layout, container, false)

        val address = view.findViewById<TextView>(R.id.address_text)
        val runtime = view.findViewById<TextView>(R.id.runtime_text)
        val phone = view.findViewById<TextView>(R.id.phone_text)
        val seat = view.findViewById<TextView>(R.id.seat_text)


        address.text = RestaurantActivity.address
        runtime.text = RestaurantActivity.runtime
        phone.text = RestaurantActivity.phone
        seat.text = RestaurantActivity.seat.toString()



        return  view
    }

}