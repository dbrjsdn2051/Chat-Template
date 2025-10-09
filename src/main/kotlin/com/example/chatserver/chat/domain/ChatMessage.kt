package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
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

    val chatRoomId by ChatRooms.id
    val memberId by Members.id
    val content by ChatMessages.content
    override var createdAt by ChatMessages.createdAt
    override var updatedAt by ChatMessages.updatedAt
}

