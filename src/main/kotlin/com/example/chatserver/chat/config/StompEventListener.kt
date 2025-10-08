package com.example.chatserver.chat.config

import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.ConcurrentHashMap

@Component
class StompEventListener {

    private val sessions = ConcurrentHashMap.newKeySet<String>()

    @EventListener
    fun connectHandle(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        sessions.add(accessor.sessionId)
        println("connectHandle: ${accessor.sessionId}")
    }

    @EventListener
    fun disconnectHandle(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        sessions.remove(accessor.sessionId)
        println("disconnectHandle: ${accessor.sessionId}")
    }
}