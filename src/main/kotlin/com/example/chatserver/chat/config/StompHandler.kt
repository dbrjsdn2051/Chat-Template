package com.example.chatserver.chat.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import java.security.Key
import javax.crypto.spec.SecretKeySpec

@Component
class StompHandler : ChannelInterceptor {

    @Value("\${jwt.secretKey}")
    private val secretKey: String? = null

    companion object {
        const val BEARER_PREFIX = "Bearer "
    }

    private val key: Key by lazy {
        SecretKeySpec(
            java.util.Base64.getDecoder().decode(secretKey),
            SignatureAlgorithm.HS256.jcaName
        )
    }

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel
    ): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (accessor.command == StompCommand.CONNECT) {
            val bearerToken = accessor.getFirstNativeHeader("Authorization")
                ?: throw IllegalArgumentException("No bearer token found")
            val token = bearerToken.substringAfter(BEARER_PREFIX)
            println("token: $token")

            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body;
            println("token parsed")
        }

        return message
    }
}