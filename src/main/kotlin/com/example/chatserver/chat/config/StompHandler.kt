package com.example.chatserver.chat.config

import com.example.chatserver.chat.service.ChatService
import com.example.chatserver.common.auth.JwtTokenProvider
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val chatService: ChatService
) : ChannelInterceptor {

    companion object {
        const val BEARER_PREFIX = "Bearer "
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel
    ): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (accessor.command == StompCommand.CONNECT) {
            val bearerToken = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER)
                ?: throw IllegalArgumentException("No bearer token found")
            val token = bearerToken.substringAfter(BEARER_PREFIX)
            jwtTokenProvider.validateToken(token)
            println("token parsed: $token")
        }

        if (accessor.command == StompCommand.SUBSCRIBE) {
            val bearerToken = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER)
                ?: throw IllegalArgumentException("No bearer token found")
            val token = bearerToken.substringAfter(BEARER_PREFIX)
            val email = jwtTokenProvider.getEmailFromToken(token)
            val roomId = accessor.destination!!.split("/")[2].toLong()
            require(chatService.isRoomParticipant(email, roomId)){
                "not a room participant"
            }
        }

        return message
    }
}