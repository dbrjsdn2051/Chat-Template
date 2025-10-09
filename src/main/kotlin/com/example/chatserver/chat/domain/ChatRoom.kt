package com.example.chatserver.chat.domain

import com.example.chatserver.common.domain.BaseEntity
import com.example.chatserver.common.domain.BaseEntityClass
import com.example.chatserver.common.domain.BaseTable
import org.jetbrains.exposed.dao.id.EntityID

object ChatRooms : BaseTable("chat_rooms") {
    val name = varchar("name", 50)
    val isGroupChat = bool("is_group_chat").default(false)
}

class ChatRoom(id: EntityID<Long>) : BaseEntity(id, ChatRooms) {
    companion object : BaseEntityClass<ChatRoom>(ChatRooms)

    var name by ChatRooms.name
    var isGroupChat by ChatRooms.isGroupChat
}

data class CreateChatRoom(
    val name: String,
    val isGroupChat: Boolean = false
)

