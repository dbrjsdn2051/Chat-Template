package com.example.chatserver.chat.service

import com.example.chatserver.chat.controller.dto.ChatMessageReqDto
import com.example.chatserver.chat.domain.CreateChatMessage
import com.example.chatserver.chat.domain.CreateChatParticipant
import com.example.chatserver.chat.domain.CreateChatRoom
import com.example.chatserver.chat.domain.CreateReadStatus
import com.example.chatserver.chat.domain.repository.ChatMessageRepository
import com.example.chatserver.chat.domain.repository.ChatParticipantRepository
import com.example.chatserver.chat.domain.repository.ChatRoomRepository
import com.example.chatserver.chat.domain.repository.ReadStatusRepository
import com.example.chatserver.domain.MemberRepository
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val readStatusRepository: ReadStatusRepository,
    private val memberRepository: MemberRepository
) {
    fun save(roomId: Long, chatMessageReqDto: ChatMessageReqDto) {
        val chatRoom = chatRoomRepository.findById(roomId)
            ?: throw NoSuchElementException("ChatRoom cannot be found")
        val member = memberRepository.findByEmail(chatMessageReqDto.senderEmail)
            ?: throw NoSuchElementException("Member cannot be found")

        val chatMessage = CreateChatMessage(
            chatRoomId = chatRoom.id.value,
            memberId = member.id.value,
            content = chatMessageReqDto.message
        )
        val chatMessageId = chatMessageRepository.save(chatMessage)
        val chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom.id.value)
        for (chatParticipant in chatParticipants) {
            val readStatus = CreateReadStatus(
                chatRoomId = chatRoom.id.value,
                memberId = chatParticipant.member.id.value,
                chatMessageId = chatMessageId,
                isRead = chatParticipant.member.id.value == member.id.value
            )
            readStatusRepository.save(readStatus)
        }
    }

    fun createGroupRoom(roomName: String, senderEmail: String) {
        val member = (memberRepository.findByEmail(senderEmail)
            ?: throw NoSuchElementException("Member cannot be found"))
        val chatRoom = chatRoomRepository.save(CreateChatRoom(roomName, true))

        chatParticipantRepository.save(CreateChatParticipant(chatRoomId = chatRoom.id.value, memberId = member.id.value))
    }

}