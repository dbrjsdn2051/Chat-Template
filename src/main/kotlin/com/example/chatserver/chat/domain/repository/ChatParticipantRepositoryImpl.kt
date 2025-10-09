package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatParticipant
import com.example.chatserver.chat.domain.ChatParticipants
import com.example.chatserver.chat.domain.CreateChatParticipant
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ChatParticipantRepositoryImpl : ChatParticipantRepository {

    override fun findByChatRoom(roomId: Long): List<ChatParticipant> = transaction {
        ChatParticipant.find { ChatParticipants.chatRoomId eq roomId }.toList()
    }

    override fun save(createChatParticipant: CreateChatParticipant): Long = transaction {
        ChatParticipants.insert {
            it[chatRoomId] = createChatParticipant.chatRoomId
            it[memberId] = createChatParticipant.memberId
        }[ChatParticipants.id].value
    }

    override fun existByMemberWithChatRoom(memberId: Long, roomId: Long): Boolean = transaction {
        ChatParticipant.find { (ChatParticipants.memberId eq memberId) and (ChatParticipants.chatRoomId eq roomId) }
            .limit(1).empty().not()
    }
}