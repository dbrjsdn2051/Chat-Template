package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
import com.example.chatserver.domain.Member
import com.example.chatserver.domain.Members
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ChatParticipants : BaseTable("chat_participants"){
    val chatRoomId = reference("chat_room_id", ChatRooms.id, onDelete = ReferenceOption.CASCADE)
    val memberId = reference("member_id", Members.id)
}

class ChatParticipant(id: EntityID<Long>) : LongEntity(id), Auditable{

    companion object : LongEntityClass<ChatParticipant>(ChatParticipants)

    var chatRoom by ChatRoom referencedOn ChatParticipants.chatRoomId
    var member by Member referencedOn ChatParticipants.memberId
    override var createdAt by ChatParticipants.createdAt
    override var updatedAt by ChatParticipants.updatedAt
}

data class CreateChatParticipant(
    val chatRoomId: Long,
    val memberId: Long
)