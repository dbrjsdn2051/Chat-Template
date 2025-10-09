package com.example.chatserver.chat.controller.dto

data class MyChatListResDto (
    val roomId: Long,
    val roomName: String,
    val isGroupChat: Boolean,
    val unReadCount: Long
){
}