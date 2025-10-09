package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
import com.example.chatserver.domain.Member
import com.example.chatserver.domain.Members
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ChatMessages : BaseTable("chat_messages"){
    val chatRoomId = reference("chat_room_id", ChatRooms.id, onDelete = ReferenceOption.CASCADE)
    val memberId = reference("member_id", Members.id)
    val content = varchar("content", 500)
}

class ChatMessage(id: EntityID<Long>) : LongEntity(id), Auditable{
    companion object : LongEntityClass<ChatMessage>(ChatMessages)

    var chatRoom by ChatRoom referencedOn ChatMessages.chatRoomId
    var member by Member referencedOn ChatMessages.memberId
    var content by ChatMessages.content
    override var createdAt by ChatMessages.createdAt
    override var updatedAt by ChatMessages.updatedAt
}

data class CreateChatMessage(
    val chatRoomId: Long,
    val memberId: Long,
    val content: String
)

