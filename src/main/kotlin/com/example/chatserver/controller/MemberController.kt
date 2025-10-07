package com.example.chatserver.controller

import com.example.chatserver.common.auth.JwtTokenProvider
import com.example.chatserver.controller.dto.MemberListRespDto
import com.example.chatserver.controller.dto.MemberLoginReqDto
import com.example.chatserver.controller.dto.MemberLoginRespDto
import com.example.chatserver.controller.dto.MemberSaveReqDto
import com.example.chatserver.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/member/create")
    fun createMember(@RequestBody memberSaveReqDto: MemberSaveReqDto): ResponseEntity<Long> {
        val member = memberService.create(memberSaveReqDto)
        return ResponseEntity<Long>(member.id.value, HttpStatus.CREATED)
    }

    @PostMapping("/member/doLogin")
    fun doLogin(@RequestBody memberLoginReqDto: MemberLoginReqDto): ResponseEntity<MemberLoginRespDto> {
        val member = memberService.login(memberLoginReqDto)
        val generateToken = jwtTokenProvider.generateToken(member.email, member.role)
        return ResponseEntity<MemberLoginRespDto>(MemberLoginRespDto(member.id.value, generateToken), HttpStatus.OK)
    }

    @GetMapping("/member/list")
    fun memberList(): ResponseEntity<List<MemberListRespDto>> {
        val listRespDtos = memberService.findAll().map { MemberListRespDto(it.id.value, it.name, it.email) }
        return ResponseEntity<List<MemberListRespDto>>(listRespDtos, HttpStatus.OK)
    }
}