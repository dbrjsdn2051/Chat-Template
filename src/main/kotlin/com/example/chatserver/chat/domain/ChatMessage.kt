package com.example.chatserver.chat.domain

import com.example.chatserver.common.domain.BaseEntity
import com.example.chatserver.common.domain.BaseEntityClass
import com.example.chatserver.common.domain.BaseTable
import com.example.chatserver.domain.Member
import com.example.chatserver.domain.Members
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object ChatMessages : BaseTable("chat_messages"){
    val chatRoomId = reference("chat_room_id", ChatRooms.id, onDelete = ReferenceOption.CASCADE)
    val memberId = reference("member_id", Members.id)
    val content = varchar("content", 500)
}

class ChatMessage(id: EntityID<Long>) : BaseEntity(id, ChatMessages){
    companion object : BaseEntityClass<ChatMessage>(ChatMessages)

    var chatRoom by ChatRoom referencedOn ChatMessages.chatRoomId
    var member by Member referencedOn ChatMessages.memberId
    var content by ChatMessages.content
}

data class CreateChatMessage(
    val chatRoomId: Long,
    val memberId: Long,
    val content: String
)

