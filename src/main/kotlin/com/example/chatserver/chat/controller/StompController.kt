package com.example.chatserver.chat.controller

import com.example.chatserver.chat.controller.dto.ChatMessageDto
import com.example.chatserver.chat.service.ChatService
import com.example.chatserver.chat.service.RedisPubSubService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class StompController(
    private val chatService: ChatService,
    private val redisPubSubService: RedisPubSubService
) {
    @MessageMapping("/{roomId}")
    fun sendMessage(@DestinationVariable roomId: Long, chatMessageDto: ChatMessageDto) {
        println("message: ${chatMessageDto.message}")
        chatService.save(roomId, chatMessageDto)
        chatMessageDto.setRoomId(roomId)
        val jsonMessage = ObjectMapper().writeValueAsString(chatMessageDto)
        redisPubSubService.publish("chat", jsonMessage)
    }
}