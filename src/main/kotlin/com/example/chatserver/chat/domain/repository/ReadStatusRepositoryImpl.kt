package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.CreateReadStatus
import com.example.chatserver.chat.domain.ReadStatus
import com.example.chatserver.chat.domain.ReadStatusEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository

@Repository
class ReadStatusRepositoryImpl : ReadStatusRepository {
    override fun save(createReadStatus: CreateReadStatus): Long = transaction {
        ReadStatus.insert {
            it[chatRoomId] = createReadStatus.chatRoomId
            it[memberId] = createReadStatus.memberId
            it[chatMessageId] = createReadStatus.chatMessageId
            it[isRead] = createReadStatus.isRead
        }[ReadStatus.id].value
    }

    override fun markAsRead(chatRoomId: Long, memberId: Long): Unit = transaction {
        ReadStatus.update({
            (ReadStatus.chatRoomId eq chatRoomId) and (ReadStatus.memberId eq memberId)
        }) {
            it[isRead] = true
        }
    }

    override fun countByChatRoomAndMemberAndIsReadFalse(chatRoomId: Long, memberId: Long): Long = transaction {
        ReadStatusEntity.find { ReadStatus.chatRoomId eq chatRoomId and (ReadStatus.memberId eq memberId) and (ReadStatus.isRead eq false) }
            .count()
    }
}