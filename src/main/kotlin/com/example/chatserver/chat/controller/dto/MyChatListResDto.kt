package com.example.chatserver.chat.controller.dto

import com.example.chatserver.chat.domain.ChatParticipant

data class MyChatListResDto(
    val roomId: Long,
    val roomName: String,
    val isGroupChat: Boolean,
    val unReadCount: Long
) {
    companion object {
        fun of(chatParticipant: ChatParticipant, readCount: Long): MyChatListResDto {
            return MyChatListResDto(
                chatParticipant.chatRoom.id.value,
                chatParticipant.chatRoom.name,
                chatParticipant.chatRoom.isGroupChat,
                readCount
            )
        }
    }
}