package com.example.univstreet

import retrofit2.Call
import retrofit2.http.GET

interface Service {
    //@GET("Api_Push.asp?{\"req\":\"servicecode_check\",\"cpcode\":\"NC001\"}")
    @GET("index_cafe.php")
    fun ApiService(): Call<infor>
}