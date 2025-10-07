package com.example.chatserver.controller

import com.example.chatserver.controller.dto.MemberSaveReqDto
import com.example.chatserver.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController (
    private val memberService: MemberService
){

    @PostMapping("/member/create")
    fun createMember(@RequestBody memberSaveReqDto: MemberSaveReqDto) : ResponseEntity<Long>{
        val member = memberService.create(memberSaveReqDto)
        return ResponseEntity<Long>(member.id.value, HttpStatus.CREATED)
    }
}