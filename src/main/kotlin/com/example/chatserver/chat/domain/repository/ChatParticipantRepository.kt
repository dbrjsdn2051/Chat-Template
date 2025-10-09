package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatParticipant
import com.example.chatserver.chat.domain.CreateChatParticipant
import org.springframework.stereotype.Repository

@Repository
interface ChatParticipantRepository {

    fun findByChatRoom(roomId: Long) : List<ChatParticipant>
    fun save(createChatParticipant: CreateChatParticipant) : Long
}