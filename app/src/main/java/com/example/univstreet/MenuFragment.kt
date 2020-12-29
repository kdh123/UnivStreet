package com.example.univstreet

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.china_food_fragment_layout.*
import kotlinx.android.synthetic.main.menu_fragment_layout.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MenuFragment : Fragment()  {

    var menuList = arrayListOf<Menu>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.menu_fragment_layout, container, false)

        restaurantInfo(RestaurantActivity.id)

        return  view
    }

    class MenuListAdapter (val context: Context, val menuList: ArrayList<Menu>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
            val view: View = LayoutInflater.from(context).inflate(R.layout.menu_item, null)

            /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
            val name = view.findViewById<TextView>(R.id.name_text)
            val price = view.findViewById<TextView>(R.id.price_Text)

            val menu = menuList[position]

            name.text = menu.name
            price.text = menu.price.toString()

            return view
        }

        override fun getItem(position: Int): Any {
            return menuList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return menuList.size
        }

    }


    fun restaurantInfo(id : String){

        try {

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
            val retrofit = retClient.MenuApi(id)
            retrofit.enqueue(object : Callback<infor> {
                override fun onFailure(call: Call<infor>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<infor>, response: Response<infor>) {
                    if(response.body() != null){

                        if(response.body()?.result?.size!! > 0){

                            for(i in 0 until response.body()!!.result.size){

                                val name : String = response.body()!!.result.get(i).get("name")!!
                                val price : Int = response.body()!!.result.get(i).get("price")!!.toInt()

                                val menu = Menu(name, price)

                                menuList.add(menu)

                            }

                            val menuAdapter = MenuListAdapter(context!!, menuList)
                            menu_listview.adapter = menuAdapter

                        } else{
                            Toast.makeText(context, "오류", Toast.LENGTH_LONG).show()
                        }

                    }
                }
            })

        } catch (e : Exception){
            Toast.makeText(context, "오류", Toast.LENGTH_LONG).show()
        }

    }

}