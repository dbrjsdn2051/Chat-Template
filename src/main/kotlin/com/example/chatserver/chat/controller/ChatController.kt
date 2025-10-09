package com.example.chatserver.chat.controller

import com.example.chatserver.chat.controller.dto.ChatMessageDto
import com.example.chatserver.chat.controller.dto.ChatRoomListResDto
import com.example.chatserver.chat.controller.dto.GroupChatRoomCreateReqDto
import com.example.chatserver.chat.controller.dto.MyChatListResDto
import com.example.chatserver.chat.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
        println(authentication.name)
        val groupRoomId = chatService.createGroupRoom(roomName, authentication.name)
        return ResponseEntity.ok(GroupChatRoomCreateReqDto(groupRoomId))
    }

    @GetMapping("/chat/room/group/list")
    fun getGroupChatRooms(): ResponseEntity<List<ChatRoomListResDto>> {
        val groupChatRooms = chatService.getGroupChatRooms()
            .map { ChatRoomListResDto(it.id.value, it.name) }
        return ResponseEntity<List<ChatRoomListResDto>>(groupChatRooms, HttpStatus.OK)
    }

    @PostMapping("/chat/room/group/{roomId}/join")
    fun joinGroupChatRoom(@PathVariable roomId: Long, authentication: Authentication): ResponseEntity<Unit> {
        chatService.addParticipantToGroupChat(roomId, authentication.name)
        return ResponseEntity.ok().build<Unit>()
    }

    @GetMapping("/chat/history/{roomId}")
    fun getChatHistory(
        @PathVariable roomId: Long,
        authentication: Authentication
    ): ResponseEntity<List<ChatMessageDto>> {
        val chatHistory = chatService.getChatHistory(roomId, authentication.name)
        return ResponseEntity<List<ChatMessageDto>>(chatHistory, HttpStatus.OK)
    }

    @PostMapping("/chat/room/{roomId}/read")
    fun messageRead(
        @PathVariable roomId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        chatService.messageRead(roomId, authentication.name)
        return ResponseEntity.ok().build<Unit>()
    }

    @GetMapping("/chat/my/rooms")
    fun getMyChatRooms(authentication: Authentication) : ResponseEntity<List<MyChatListResDto>>{
        val mychatListResDtos = chatService.getMyChatRooms(authentication.name)
        return ResponseEntity<List<MyChatListResDto>>(mychatListResDtos, HttpStatus.OK)
    }

    @DeleteMapping("/chat/room/{roomId}/leave")
    fun leaveGroupChatRoom(@PathVariable roomId: Long, authentication: Authentication): ResponseEntity<Unit> {
        chatService.leaveGroupChatRoom(roomId, authentication.name)
        return ResponseEntity.ok().build<Unit>()
    }

}