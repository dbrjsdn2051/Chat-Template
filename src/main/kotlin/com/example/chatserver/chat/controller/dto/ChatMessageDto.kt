package com.example.chatserver.chat.controller.dto

data class ChatMessageDto(
    val message: String,
    val senderEmail: String,
    val roomId: Long? = null
) {
    fun setRoomId(roomId: Long): ChatMessageDto {
        return ChatMessageDto(message, senderEmail, roomId)
    }
}