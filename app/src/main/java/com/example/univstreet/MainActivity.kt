package com.example.univstreet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        china_food.setOnClickListener{
            restaurantInfo("중국집")
        }

        chicken.setOnClickListener{
            restaurantInfo("치킨")
        }

        ramen.setOnClickListener{
            restaurantInfo("분식")
        }

        cafe.setOnClickListener{
            restaurantInfo("카페")
        }

        insert_button.setOnClickListener{

            try{

                val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
                val retrofit = retClient.ApiService2("Gloriaaaaa", "USAaaa")
                retrofit.enqueue(object : Callback<resultResponse> {
                    override fun onFailure(call: Call<resultResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                    override fun onResponse(call: Call<resultResponse>, response: Response<resultResponse>) {
                        if(response.body()!=null){

                            val result = "result : ${response.body()?.result}"
                            Toast.makeText(applicationContext, result, Toast.LENGTH_LONG).show()

                        }
                    }
                })

            } catch (e : Exception){

                Toast.makeText(applicationContext, "삽입 실패!", Toast.LENGTH_LONG).show()
            }

        }


    }


    fun restaurantInfo(menu : String){

        var intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("menu", menu);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }





}
