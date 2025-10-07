package com.example.chatserver.domain

import com.example.chatserver.controller.dto.MemberSaveReqDto
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository {
    fun save(memberSaveReqDto: MemberSaveReqDto): Member
    fun findByEmail(email: String): Member?
}