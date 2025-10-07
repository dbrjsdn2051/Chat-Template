package com.example.chatserver.controller.dto

data class MemberSaveReqDto (
    val name: String,
    val email: String,
    val password: String
)