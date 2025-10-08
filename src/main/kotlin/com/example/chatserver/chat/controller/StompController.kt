package com.example.chatserver.chat.controller

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class StompController {

    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    fun sendMessage(@DestinationVariable roomId: Long, message: String) : String{
        println("message: $message")
        return message;
    }
}