package com.example.kbotapp.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kbotapp.Message
import com.example.kbotapp.MessageList
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    var messages by mutableStateOf(MessageList.getAll())


    fun addMessage(message: Message){
        messages = listOf(message) + messages
    }

    init {
        viewModelScope.launch {
            val msg = Message(
                "Bienvenue, Je suis KBot! Un bot à multiusages. \nEntrez \"Aide\" pour voir mes commandes.",
                false
            )
            addMessage(msg)
        }
    }
}