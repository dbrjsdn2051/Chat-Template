package com.example.chatserver.service

import com.example.chatserver.controller.dto.MemberLoginReqDto
import com.example.chatserver.controller.dto.MemberSaveReqDto
import com.example.chatserver.domain.Member
import com.example.chatserver.domain.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(memberSaveReqDto: MemberSaveReqDto): Member {
        memberRepository.findByEmail(memberSaveReqDto.email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        memberSaveReqDto.password = passwordEncoder.encode(memberSaveReqDto.password)
        return memberRepository.save(memberSaveReqDto)
    }

    fun login(memberLoginReqDto: MemberLoginReqDto) : Member {
        val member = (memberRepository.findByEmail(memberLoginReqDto.email)
            ?: throw IllegalArgumentException("이메일이 존재하지 않습니다."))

        if(!passwordEncoder.matches(memberLoginReqDto.password, member.password)){
            throw IllegalArgumentException("패스워드가 맞지 않습니다.")
        }

        return member
    }

    fun findAll(): List<Member>{
        return memberRepository.findAll()
    }


}