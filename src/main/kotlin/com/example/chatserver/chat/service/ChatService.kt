package com.example.chatserver.chat.service

import com.example.chatserver.chat.controller.dto.ChatMessageDto
import com.example.chatserver.chat.controller.dto.MyChatListResDto
import com.example.chatserver.chat.domain.ChatRoom
import com.example.chatserver.chat.domain.CreateChatMessage
import com.example.chatserver.chat.domain.CreateChatParticipant
import com.example.chatserver.chat.domain.CreateChatRoom
import com.example.chatserver.chat.domain.CreateReadStatus
import com.example.chatserver.chat.domain.repository.ChatMessageRepository
import com.example.chatserver.chat.domain.repository.ChatParticipantRepository
import com.example.chatserver.chat.domain.repository.ChatRoomRepository
import com.example.chatserver.chat.domain.repository.ReadStatusRepository
import com.example.chatserver.domain.MemberRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val readStatusRepository: ReadStatusRepository,
    private val memberRepository: MemberRepository
) {
    fun save(roomId: Long, chatMessageDto: ChatMessageDto) {
        transaction {
            val chatRoom = chatRoomRepository.findById(roomId)
                ?: throw NoSuchElementException("ChatRoom cannot be found")
            val member = memberRepository.findByEmail(chatMessageDto.senderEmail)
                ?: throw NoSuchElementException("Member cannot be found")

            val chatMessage = CreateChatMessage(
                chatRoomId = chatRoom.id.value,
                memberId = member.id.value,
                content = chatMessageDto.message
            )
            val chatMessageId = chatMessageRepository.save(chatMessage)
            val chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom.id.value)
            for (chatParticipant in chatParticipants) {
                val readStatus = CreateReadStatus(
                    chatRoomId = chatRoom.id.value,
                    memberId = chatParticipant.member.id.value, // lazy loading
                    chatMessageId = chatMessageId,
                    isRead = chatParticipant.member.id.value == member.id.value
                )
                readStatusRepository.save(readStatus)
            }
        }
    }

    fun createGroupRoom(roomName: String, senderEmail: String): Long {
        val member = memberRepository.findByEmail(senderEmail)
            ?: throw NoSuchElementException("Member cannot be found")

        val chatRoomId = chatRoomRepository.save(CreateChatRoom(roomName, true))

        return chatParticipantRepository.save(
            CreateChatParticipant(
                chatRoomId = chatRoomId,
                memberId = member.id.value
            )
        )
    }

    fun getGroupChatRooms(): List<ChatRoom> {
        return chatRoomRepository.findByIsGroupTrue()
    }

    fun addParticipantToGroupChat(roomId: Long, senderEmail: String) {
        val chatRoom = (chatRoomRepository.findById(roomId)
            ?: throw NoSuchElementException("ChatRoom cannot be found"))

        val member = (memberRepository.findByEmail(senderEmail)
            ?: throw NoSuchElementException("Member cannot be found"))

        if (chatParticipantRepository.existByMemberWithChatRoom(member.id.value, chatRoom.id.value)) {
            return
        }

        chatParticipantRepository.save(
            CreateChatParticipant(
                chatRoomId = chatRoom.id.value,
                memberId = member.id.value
            )
        )
    }

    fun getChatHistory(roomId: Long, senderEmail: String): List<ChatMessageDto> {
        return transaction {
            val chatRoom =
                chatRoomRepository.findById(roomId) ?: throw NoSuchElementException("ChatRoom cannot be found")
            val member =
                memberRepository.findByEmail(senderEmail) ?: throw NoSuchElementException("Member cannot be found")
            val roomParticipants = chatParticipantRepository.findByChatRoom(chatRoom.id.value)
            roomParticipants.find { it.member.id.value == member.id.value }
                ?: throw NoSuchElementException("Member not in chatroom")

            chatMessageRepository.findByChatRoom(chatRoom.id.value)
                .map { ChatMessageDto(it.content, it.member.email) }
        }
    }

    fun isRoomParticipant(email: String, roomId: Long): Boolean {
        return transaction {
            val member = memberRepository.findByEmail(email)
                ?: throw NoSuchElementException("Member cannot be found")
            val chatRoom = chatRoomRepository.findById(roomId)
                ?: throw NoSuchElementException("ChatRoom cannot be found")
            val chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom.id.value)
            chatParticipants.find { it.member.id.value == member.id.value } != null
        }
    }

    fun messageRead(roomId: Long, email: String) {
        val chatRoom = chatRoomRepository.findById(roomId) ?: throw NoSuchElementException("ChatRoom cannot be found")
        val member = memberRepository.findByEmail(email) ?: throw NoSuchElementException("Member cannot be found")
        readStatusRepository.markAsRead(chatRoom.id.value, member.id.value)
    }

    fun getMyChatRooms(email: String) : List<MyChatListResDto>{
        return transaction {
        val member = memberRepository.findByEmail(email) ?: throw NoSuchElementException("Member cannot be found")
            chatParticipantRepository.findAllByMember(member.id.value)
            .map {
                MyChatListResDto(
                it.chatRoom.id.value,
                it.chatRoom.name,
                it.chatRoom.isGroupChat,
                readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(it.chatRoom.id.value, it.member.id.value)
                )
            }
        }
    }

    fun leaveGroupChatRoom(roomId: Long, email: String) {
        val chatRoom = chatRoomRepository.findById(roomId) ?: throw NoSuchElementException("ChatRoom cannot be found")
        val member = memberRepository.findByEmail(email) ?: throw NoSuchElementException("Member cannot be found")
        require(chatRoom.isGroupChat) { "This is not group chat room." }
        chatParticipantRepository.findByChatRoomAndMember(chatRoom.id.value, member.id.value)?.let {
            chatParticipantRepository.delete(it.id.value)
        }

        if (chatParticipantRepository.findByChatRoom(chatRoom.id.value).isEmpty()) {
            chatRoomRepository.delete(chatRoom.id.value)
        }
    }
}