package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.ChatRooms
import com.example.chatserver.chat.domain.CreateChatRoom
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatRoomRepositoryImpl : ChatRoomRepository {
    override fun findById(id: Long): ChatRoom? = transaction {
        ChatRoom.findById(id)
    }

    override fun save(createChatRoom: CreateChatRoom): Long = transaction {
        ChatRooms.insert {
            it[name] = createChatRoom.name
            it[isGroupChat] = createChatRoom.isGroupChat
        }[ChatRooms.id].value
    }

    override fun findByIsGroupTrue(): List<ChatRoom> = transaction {
        ChatRoom.find { ChatRooms.isGroupChat eq true }.toList()
    }
}