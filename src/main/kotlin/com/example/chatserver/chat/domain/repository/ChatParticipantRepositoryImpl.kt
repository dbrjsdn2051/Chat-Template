package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatParticipant
import com.example.chatserver.chat.domain.ChatParticipants
import com.example.chatserver.chat.domain.ChatRooms
import com.example.chatserver.chat.domain.CreateChatParticipant
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    override fun findAllByMember(memberId: Long): List<ChatParticipant> = transaction {
        ChatParticipant.find { ChatParticipants.memberId eq memberId }.toList()
    }

    override fun findByChatRoomAndMember(
        chatRoomId: Long,
        memberId: Long
    ): ChatParticipant? = transaction {
        ChatParticipant.find { (ChatParticipants.chatRoomId eq chatRoomId) and (ChatParticipants.memberId eq memberId) }
            .firstOrNull()
    }

    override fun delete(chatParticipantId: Long): Unit = transaction {
        ChatParticipant.findById(chatParticipantId)?.delete()
    }

    override fun findExistingPrivateRoom(
        otherMemberId: Long,
        myId: Long
    ): Long? = transaction {
        val c1 = ChatParticipants.alias("c1")
        val c2 = ChatParticipants.alias("c2")


        c1.join(
            otherTable = c2,
            joinType = JoinType.INNER,
            onColumn = c1[ChatParticipants.chatRoomId],
            otherColumn = c2[ChatParticipants.chatRoomId]
        ).join(
            otherTable = ChatRooms,
            joinType = JoinType.INNER,
            onColumn = c1[ChatParticipants.chatRoomId],
            otherColumn = ChatRooms.id
        )
            .selectAll()
            .where {
                (c1[ChatParticipants.memberId] eq otherMemberId) and
                        (c2[ChatParticipants.memberId] eq myId) and
                        (ChatRooms.isGroupChat eq false)
            }
            .firstOrNull()?.get(c1[ChatParticipants.chatRoomId])?.value
    }
}