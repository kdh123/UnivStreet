package com.example.univstreet

//data class infor(val SERVERIP : String, val Mode : String)
data class infor(val result : ArrayList<HashMap<String, String>>)
data class insertInfor(val name : String, val address : String)
data class resultResponse(val result : String)