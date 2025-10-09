package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatMessage
import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateChatMessage
import com.example.chatserver.domain.Member
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatMessageRepositoryImpl : ChatMessageRepository {

    override fun save(createChatMessage: CreateChatMessage): Long = transaction {
        ChatMessage.new {
            this.chatRoom = ChatRoom[createChatMessage.chatRoomId]
            this.member = Member[createChatMessage.memberId]
            this.content = createChatMessage.content
        }.id.value
    }
}