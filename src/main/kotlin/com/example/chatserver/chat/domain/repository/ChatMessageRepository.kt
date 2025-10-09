package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.CreateChatMessage
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository {
    fun save(createChatMessage: CreateChatMessage) : Long
}