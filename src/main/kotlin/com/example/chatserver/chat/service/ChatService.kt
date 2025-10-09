package com.example.chatserver.chat.service

import com.example.chatserver.chat.domain.repository.ChatMessageRepository
import com.example.chatserver.chat.domain.repository.ChatParticipantRepository
import com.example.chatserver.chat.domain.repository.ChatRoomRepository
import com.example.chatserver.chat.domain.repository.ReadStatusRepository
import org.springframework.stereotype.Service

@Service
class ChatService (
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val readStatusRepository: ReadStatusRepository
){

}