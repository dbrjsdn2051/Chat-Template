package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.CreateReadStatus
import com.example.chatserver.chat.domain.ReadStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ReadStatusRepositoryImpl : ReadStatusRepository {
    override fun save(createReadStatus: CreateReadStatus): Long = transaction{
        ReadStatus.insert {
            it[chatRoomId] = createReadStatus.chatRoomId
            it[memberId] = createReadStatus.memberId
            it[chatMessageId] = createReadStatus.chatMessageId
            it[isRead] = createReadStatus.isRead
        }[ReadStatus.id].value
    }
}