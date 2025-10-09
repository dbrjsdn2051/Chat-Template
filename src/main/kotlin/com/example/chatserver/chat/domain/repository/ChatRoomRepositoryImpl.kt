package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateChatRoom
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatRoomRepositoryImpl : ChatRoomRepository {
    override fun findById(id: Long): ChatRoom? = transaction{
        ChatRoom.findById(id)
    }

    override fun save(createChatRoom: CreateChatRoom): ChatRoom = transaction{
        ChatRoom.new {
            this.name = createChatRoom.name
            this.isGroupChat = createChatRoom.isGroupChat
        }
    }
}