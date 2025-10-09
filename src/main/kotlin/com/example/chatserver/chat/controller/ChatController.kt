package com.example.chatserver.chat.controller

import com.example.chatserver.chat.controller.dto.GroupChatRoomCreateReqDto
import com.example.chatserver.chat.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/chat/room/group/create")
    fun createGroupRoom(
        @RequestParam roomName: String,
        authentication: Authentication
    ): ResponseEntity<GroupChatRoomCreateReqDto> {
        val groupRoomId = chatService.createGroupRoom(roomName, authentication.name)
        return ResponseEntity.ok(GroupChatRoomCreateReqDto(groupRoomId))
    }
}