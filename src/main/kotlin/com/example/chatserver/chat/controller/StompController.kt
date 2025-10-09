package com.example.chatserver.chat.controller

import com.example.chatserver.chat.controller.dto.ChatMessageDto
import com.example.chatserver.chat.service.ChatService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class StompController(
    private val messageTemplate: SimpMessageSendingOperations,
    private val chatService: ChatService
) {
    @MessageMapping("/{roomId}")
    fun sendMessage(@DestinationVariable roomId: Long, chatMessageDto: ChatMessageDto) {
        println("message: ${chatMessageDto.message}")
        chatService.save(roomId, chatMessageDto)
        messageTemplate.convertAndSend("/topic/$roomId", chatMessageDto)
    }
}