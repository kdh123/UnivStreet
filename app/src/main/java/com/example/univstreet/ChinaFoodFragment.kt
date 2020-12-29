package com.example.univstreet

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.china_food_fragment_layout.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ChinaFoodFragment : Fragment() {

    var restaurantList = arrayListOf<Restaurant>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.china_food_fragment_layout, container, false)
        restaurantInfo("중국집")

        return  view
    }

    class RestaurantListAdapter (val context: Context, val restaurantList: ArrayList<Restaurant>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
            val view: View = LayoutInflater.from(context).inflate(R.layout.restaurant_item, null)

            /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
            val restaurantLogo = view.findViewById<ImageView>(R.id.logo)
            val restaurantName = view.findViewById<TextView>(R.id.name)

            val restaurant = restaurantList[position]

            restaurantName.text = restaurant.name

            loadImg(restaurant.image, restaurantLogo)

            return view
        }

        override fun getItem(position: Int): Any {
            return restaurantList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return restaurantList.size
        }


        fun loadImg(imgName : String, imgView : ImageView){

            try{

                val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
                val retrofit = retClient.InfoImgApi(imgName)
                retrofit.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.body()!=null){

                            var inputStream  = response.body()!!.byteStream()
                            val bitmap = BitmapFactory.decodeStream(inputStream)

                            imgView.setImageBitmap(bitmap)

                            //Toast.makeText(context, "다운로드 성공", Toast.LENGTH_LONG).show()

                        }
                    }
                })

            } catch (e : Exception){

                Toast.makeText(context, "다운로드 실패!", Toast.LENGTH_LONG).show()
            }

        }

    }

    fun restaurantInfo(menu : String){

        try {

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
            val retrofit = retClient.ApiService(menu)
            retrofit.enqueue(object : Callback<infor> {
                override fun onFailure(call: Call<infor>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<infor>, response: Response<infor>) {
                    if(response.body() != null){

                        if(response.body()!!.result!!.size!! > 0){

                            for(i in 0 until response.body()!!.result.size){

                                val id : String = response.body()!!.result.get(i).get("id")!!
                                val imgUrl : String = response.body()!!.result.get(i).get("img_url")!!
                                val name : String = response.body()!!.result.get(i).get("name")!!
                                val address : String = response.body()!!.result.get(i).get("address")!!
                                val runtime : String = response.body()!!.result.get(i).get("runtime")!!
                                val phone : String = response.body()!!.result.get(i).get("phone")!!
                                val seat : Int = response.body()!!.result.get(i).get("seat")!!.toInt()

                                val restaurant = Restaurant(id, imgUrl, name, address, runtime, phone, seat)

                                restaurantList.add(restaurant)

                            }

                            val restaurantAdapter = RestaurantListAdapter(context!!, restaurantList)
                            listview.adapter = restaurantAdapter

                            listview.setOnItemClickListener{ parent, view, position, id ->
                                val id = restaurantList[position].id
                                val name = restaurantList[position].name
                                val address = restaurantList[position].address
                                val runtime = restaurantList[position].runtime
                                val phone = restaurantList[position].phone
                                val seat = restaurantList[position].seat

                                var intent = Intent(context, RestaurantActivity::class.java)
                                intent.putExtra("id", id)
                                intent.putExtra("name", name)
                                intent.putExtra("address", address)
                                intent.putExtra("runtime", runtime)
                                intent.putExtra("phone", phone)
                                intent.putExtra("seat", seat)

                                startActivity(intent)

                            }

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