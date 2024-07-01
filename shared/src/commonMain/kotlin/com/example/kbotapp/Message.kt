package com.example.kbotapp

import kotlinx.datetime.*

class Message(var text: String, var isOut: Boolean = false,  var time: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time.toString())

object MessageList{
    fun getAll(): List<Message>{
        return mutableListOf<Message>()
    }
}

