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

        try {

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
            val retrofit = retClient.ApiService("Gloria")
            retrofit.enqueue(object : Callback<infor> {
                override fun onFailure(call: Call<infor>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<infor>, response: Response<infor>) {
                    if(response.body()!=null){

                        if(response.body()?.result?.size!! > 0){
                            val result = "result : ${response.body()?.result?.get(0)?.get("address")}"
                            text1.text = result
                        } else{
                            text1.text = "없음"
                        }

                    }
                }
            })

        } catch (e : Exception){
            Toast.makeText(applicationContext, "오류", Toast.LENGTH_LONG).show()
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

        button.setOnClickListener{
            var intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("word", "hello");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

}
