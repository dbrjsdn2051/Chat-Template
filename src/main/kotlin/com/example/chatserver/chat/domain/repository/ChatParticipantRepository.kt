package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.ChatParticipant
import com.example.chatserver.chat.domain.CreateChatParticipant
import org.springframework.stereotype.Repository

@Repository
interface ChatParticipantRepository {

    fun findByChatRoom(roomId: Long) : List<ChatParticipant>
    fun save(createChatParticipant: CreateChatParticipant) : Long
    fun existByMemberWithChatRoom(memberId: Long, roomId: Long) : Boolean
    fun findAllByMember(memberId: Long) : List<ChatParticipant>
    fun findByChatRoomAndMember(chatRoomId: Long, memberId: Long) : ChatParticipant?
    fun delete(chatParticipantId: Long) : Unit
    fun findExistingPrivateRoom(otherMemberId: Long, myId: Long) : Long?
}