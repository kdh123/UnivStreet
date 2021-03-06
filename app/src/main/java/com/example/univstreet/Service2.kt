package com.example.univstreet

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Service2 {
    @FormUrlEncoded
    @POST("index_restaurants.php")
    fun ApiService(
        @Field("menu") menu: String
    ): Call<infor>

    @FormUrlEncoded
    @POST("index_menu.php")
    fun MenuApi(
        @Field("id") id: String
    ): Call<infor>

    @FormUrlEncoded
    @POST("index_review.php")
    fun ReviewApi(
        @Field("id") id: String
    ): Call<infor>

    @FormUrlEncoded
    @POST("insert.php")
    fun ApiService2(
        @Field("name") name: String,
        @Field("address") address: String
    ): Call<resultResponse>


    // 프로필 이미지 보내기
    @Multipart
    @POST("/UploadToServer.php")
    fun post_Porfile_Request(
        //@Part("userId") userId: String,
        @Part imageFile : MultipartBody.Part
    ): Call<String>



    @GET("review/{path}")
    @Streaming //용량이 적을 경우 @Streaming은 생략이 가능하다.
    fun ReviewImgApi(
        @Path("path") path:String
    ):Call<ResponseBody>


    @GET("img/{path}")
    @Streaming //용량이 적을 경우 @Streaming은 생략이 가능하다.
    fun InfoImgApi(
        @Path("path") path:String
    ):Call<ResponseBody>




}

