package com.example.chatserver.domain

import com.example.chatserver.controller.dto.MemberSaveReqDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl : MemberRepository {

    override fun save(memberSaveReqDto: MemberSaveReqDto): Member = transaction {
        Member.new {
            this.name = memberSaveReqDto.name
            this.email = memberSaveReqDto.email
            this.password = memberSaveReqDto.password
        }
    }

    override fun findByEmail(email: String): Member? = transaction {
        Member.find { Members.email eq email }.firstOrNull()
    }

    override fun findAll(): List<Member> = transaction {
        Member.all().toList()
    }
}