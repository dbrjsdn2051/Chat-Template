package com.example.chatserver.chat.controller.dto

data class ChatMessageReqDto(
    val message: String,
    val senderEmail: String
)