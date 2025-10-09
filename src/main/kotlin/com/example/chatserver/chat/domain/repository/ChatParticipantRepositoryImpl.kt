package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatParticipant
import com.example.chatserver.chat.domain.ChatParticipants
import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateChatParticipant
import com.example.chatserver.domain.Member
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatParticipantRepositoryImpl : ChatParticipantRepository {

    override fun findByChatRoom(roomId: Long): List<ChatParticipant> = transaction {
        ChatParticipant.find { ChatParticipants.chatRoomId eq roomId }.toList()
    }

    override fun save(createChatParticipant: CreateChatParticipant): ChatParticipant = transaction {
        ChatParticipant.new {
            this.chatRoom = ChatRoom[createChatParticipant.chatRoomId]
            this.member = Member[createChatParticipant.memberId]
        }
    }
}