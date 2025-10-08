package com.example.chatserver.chat.controller

import com.example.chatserver.chat.controller.dto.ChatMessageReqDto
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class StompController(
    private val messageTemplate: SimpMessageSendingOperations
) {
    @MessageMapping("/{roomId}")
    fun sendMessage(@DestinationVariable roomId: Long, chatMessageReqDto: ChatMessageReqDto) {
        println("message: ${chatMessageReqDto.message}")
        messageTemplate.convertAndSend("/topic/$roomId", chatMessageReqDto)
    }
}