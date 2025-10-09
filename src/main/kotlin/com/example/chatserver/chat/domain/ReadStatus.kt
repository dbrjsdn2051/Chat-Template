package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
import com.example.chatserver.domain.Members
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ReadStatus : BaseTable("read_status"){
    val chatRoomId = reference("chat_room_id", ChatRooms.id, onDelete = ReferenceOption.CASCADE)
    val memberId = reference("member_id", Members.id)
    val chatMessageId = reference("chat_message_id", ChatMessages.id, onDelete = ReferenceOption.CASCADE)
    val isRead = bool("is_read")
}

class ReadStatusEntity(id: EntityID<Long>) : LongEntity(id), Auditable {
    companion object : LongEntityClass<ReadStatusEntity>(ReadStatus)

    val chatRoomId by ChatRooms.id
    val memberId by Members.id
    val chatMessageId by ChatMessages.id
    val isRead by ReadStatus.isRead
    override var createdAt by ReadStatus.createdAt
    override var updatedAt by ReadStatus.updatedAt
}