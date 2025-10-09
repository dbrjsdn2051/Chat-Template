package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object ChatRooms : BaseTable("chat_rooms") {
    val name = varchar("name", 50)
    val isGroupChat = bool("is_group_chat").default(false)
}

class ChatRoom(id: EntityID<Long>) : LongEntity(id), Auditable {
    companion object : LongEntityClass<ChatRoom>(ChatRooms)


    var name by ChatRooms.name
    var isGroupChat by ChatRooms.isGroupChat
    override var createdAt by ChatRooms.createdAt
    override var updatedAt by ChatRooms.updatedAt
}

data class CreateChatRoom(
    val name: String,
    val isGroupChat: Boolean = false
)

