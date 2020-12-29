package com.example.univstreet

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.menu_fragment_layout.*
import kotlinx.android.synthetic.main.review_fragment_layout.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ReviewFragment : Fragment()  {

    var reviewList = arrayListOf<Review>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.review_fragment_layout, container, false)

        reviewInfo(RestaurantActivity.id)

        return  view
    }

    class ReviewListAdapter (val context: Context, val reviewList: ArrayList<Review>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
            val view: View = LayoutInflater.from(context).inflate(R.layout.review_item, null)

            /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
            val name = view.findViewById<TextView>(R.id.name_text)
            val date = view.findViewById<TextView>(R.id.date_text)
            val score = view.findViewById<TextView>(R.id.score_text)
            val content = view.findViewById<TextView>(R.id.content_text)
            val reviewImg = view.findViewById<ImageView>(R.id.review_img)
            val replyLayout = view.findViewById<LinearLayout>(R.id.reply_layout)
            val reply = view.findViewById<TextView>(R.id.reply_text)

            val review = reviewList[position]

            name.text = review.name
            date.text = review.date
            score.text = review.score.toString()
            content.text = review.content

            if(!review.imgUrl.isNullOrBlank()){
                reviewImg.visibility = View.VISIBLE
                loadImg(review.imgUrl!!, reviewImg)


            }

            if(!review.reply.isNullOrBlank()){
                replyLayout.visibility = View.VISIBLE
                reply.text = review.reply
            }


            return view
        }

        override fun getItem(position: Int): Any {
            return reviewList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return reviewList.size
        }

        fun loadImg(imgName : String, imgView : ImageView){

            try{

                val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
                val retrofit = retClient.ReviewImgApi(imgName)
                retrofit.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.body()!=null){

                            var inputStream  = response.body()!!.byteStream()
                            val bitmap = BitmapFactory.decodeStream(inputStream)

                            imgView.setImageBitmap(bitmap)

                            Log.e("imgDown", "imgdownload")

                        }
                    }
                })

            } catch (e : Exception){

                Toast.makeText(context, "다운로드 실패!", Toast.LENGTH_LONG).show()
            }

        }


    }




    fun reviewInfo(id : String){

        try {

            val retClient = Okhttp3RetrofitManager.getRetrofitService(Service2::class.java)
            val retrofit = retClient.ReviewApi(id)
            retrofit.enqueue(object : Callback<infor> {
                override fun onFailure(call: Call<infor>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(call: Call<infor>, response: Response<infor>) {
                    if(response.body() != null){

                        if(response.body()?.result?.size!! > 0){

                            for(i in 0 until response.body()!!.result.size){

                                val name : String = response.body()!!.result.get(i).get("name")!!
                                val date : String = response.body()!!.result.get(i).get("date")!!
                                val score : Int = response.body()!!.result.get(i).get("score")!!.toInt()
                                val content : String = response.body()!!.result.get(i).get("content")!!
                                val imgUrl : String? = response.body()!!.result.get(i).get("img_url")
                                val reply : String? = response.body()!!.result.get(i).get("reply")


                                val review = Review(name, date, score, content, imgUrl, reply)

                                reviewList.add(review)

                            }

                            val reviewAdapter = ReviewListAdapter(context!!, reviewList)
                            review_listview.adapter = reviewAdapter

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