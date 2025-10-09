package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatMessage
import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateReadStatus
import com.example.chatserver.chat.domain.ReadStatusEntity
import com.example.chatserver.domain.Member
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ReadStatusRepositoryImpl : ReadStatusRepository {
    override fun save(createReadStatus: CreateReadStatus): Long = transaction{
        ReadStatusEntity.new {
            this.chatRoom = ChatRoom[createReadStatus.chatRoomId]
            this.member = Member[createReadStatus.memberId]
            this.chatMessage = ChatMessage[createReadStatus.chatMessageId]
            this.isRead = createReadStatus.isRead
        }.id.value
    }
}