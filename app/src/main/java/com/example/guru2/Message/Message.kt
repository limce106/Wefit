package com.example.guru2.Message

data class Message(
    var message:String?,
    var sendId:String?
){
    constructor():this("", "")
}
