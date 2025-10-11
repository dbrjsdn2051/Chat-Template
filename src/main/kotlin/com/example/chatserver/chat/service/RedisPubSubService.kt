package com.example.chatserver.chat.service

import com.example.chatserver.chat.controller.dto.ChatMessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service

@Service
class RedisPubSubService(
    private val messageTemplate: SimpMessageSendingOperations,
    @Qualifier("chatPubSub") private val stringRedisTemplate: StringRedisTemplate,
) : MessageListener {

    fun publish(channel: String, message: String) {
        stringRedisTemplate.convertAndSend(channel, message)
    }

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val chatMessageDto = ObjectMapper().registerKotlinModule()
                .readValue<ChatMessageDto>(message.body, ChatMessageDto::class.java)
            messageTemplate.convertAndSend("/topic/${chatMessageDto.roomId}", chatMessageDto)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}