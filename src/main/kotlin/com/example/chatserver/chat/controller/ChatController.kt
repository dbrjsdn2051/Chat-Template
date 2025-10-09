package com.example.chatserver.chat.controller

import com.example.chatserver.chat.service.ChatService
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController (
    private val chatService: ChatService
){

}