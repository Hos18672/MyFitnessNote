package com.example.myfitneesnote.model


class ChatMessage(
    val id: String,
    val text: String,
    val formId: String,
    val toId: String,
    val timestamp: Long){
    constructor(): this("","","","",-1)
}