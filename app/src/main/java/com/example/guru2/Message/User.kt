package com.example.guru2.Message

data class User(
    var reg_id :String,
    var reg_pw :String,
    var reg_name:String,
    var reg_tel:String,
    var str_gender:String,
    var str_purpose:String,
    var uId:String
){
    constructor():this ("", "", "", "", "" ,
        "" ,"")
}
