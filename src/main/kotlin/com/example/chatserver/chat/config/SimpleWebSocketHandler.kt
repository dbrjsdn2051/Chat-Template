//package com.example.chatserver.chat.config
//
//import org.springframework.stereotype.Component
//import org.springframework.web.socket.CloseStatus
//import org.springframework.web.socket.TextMessage
//import org.springframework.web.socket.WebSocketSession
//import org.springframework.web.socket.handler.TextWebSocketHandler
//import java.util.concurrent.ConcurrentHashMap
//
//@Component
//class SimpleWebSocketHandler : TextWebSocketHandler() {
//    private val sessions = ConcurrentHashMap.newKeySet<WebSocketSession>()
//
//    override fun afterConnectionEstablished(session: WebSocketSession) {
//        sessions.add(session)
//        println("Connected : ${session.id}")
//    }
//
//    override fun handleTextMessage(
//        session: WebSocketSession,
//        message: TextMessage
//    ) {
//        val payload = message.payload
//        println("Received : $payload")
//        for (webSocketSession in sessions) {
//            if(webSocketSession.isOpen){
//                webSocketSession.sendMessage(TextMessage(payload))
//            }
//        }
//    }
//
//    override fun afterConnectionClosed(
//        session: WebSocketSession,
//        status: CloseStatus
//    ) {
//        sessions.remove(session)
//        println("Disconnected : ${session.id}")
//    }
//}