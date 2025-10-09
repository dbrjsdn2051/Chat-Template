package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatMessage
import com.example.chatserver.chat.domain.ChatMessages
import com.example.chatserver.chat.domain.CreateChatMessage
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatMessageRepositoryImpl : ChatMessageRepository {

    override fun save(createChatMessage: CreateChatMessage): Long = transaction {
        ChatMessages.insert {
            it[chatRoomId] = createChatMessage.chatRoomId
            it[memberId] = createChatMessage.memberId
            it[content] = createChatMessage.content
        }[ChatMessages.id].value
    }

    override fun findByChatRoom(roomId: Long): List<ChatMessage> = transaction {
        ChatMessage.find { ChatMessages.chatRoomId eq roomId }
            .orderBy(ChatMessages.createdAt to SortOrder.ASC)
            .toList()
    }
}