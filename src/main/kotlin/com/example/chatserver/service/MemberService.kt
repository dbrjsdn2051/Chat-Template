package com.example.chatserver.service

import com.example.chatserver.controller.dto.MemberSaveReqDto
import com.example.chatserver.domain.Member
import com.example.chatserver.domain.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun create(memberSaveReqDto: MemberSaveReqDto): Member {
        memberRepository.findByEmail(memberSaveReqDto.email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        return memberRepository.save(memberSaveReqDto)
    }
}