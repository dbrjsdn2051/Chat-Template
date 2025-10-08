//package com.example.chatserver.chat.config
//
//import org.springframework.context.annotation.Configuration
//import org.springframework.web.socket.config.annotation.EnableWebSocket
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
//
//@Configuration
//@EnableWebSocket
//class WebSocketConfig (
//    private val simpleWebSocketHandler: SimpleWebSocketHandler
//): WebSocketConfigurer{
//
//    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
//        registry.addHandler(simpleWebSocketHandler, "/connect")
//            .setAllowedOrigins("http://localhost:3000")
//    }
//}