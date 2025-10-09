package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
import com.example.chatserver.domain.Member
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

    var chatRoom by ChatRoom referencedOn ReadStatus.chatRoomId
    var member by Member referencedOn ReadStatus.memberId
    var chatMessage by ChatMessage referencedOn ReadStatus.chatMessageId
    var isRead by ReadStatus.isRead
    override var createdAt by ReadStatus.createdAt
    override var updatedAt by ReadStatus.updatedAt
}

data class CreateReadStatus(
    val chatRoomId: Long,
    val memberId: Long,
    val chatMessageId: Long,
    val isRead: Boolean
)