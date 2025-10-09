package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateChatRoom
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository {

    fun findById(id: Long): ChatRoom?
    fun save(createChatRoom: CreateChatRoom) : Long
    fun findByIsGroupTrue() : List<ChatRoom>
}