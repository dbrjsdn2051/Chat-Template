package com.example.chatserver.chat.domain

import com.example.chatserver.common.`domain `.Auditable
import com.example.chatserver.common.`domain `.BaseTable
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

    val chatRoomId by ChatRooms.id
    val memberId by Members.id
    override var createdAt by ChatParticipants.createdAt
    override var updatedAt by ChatParticipants.updatedAt
}