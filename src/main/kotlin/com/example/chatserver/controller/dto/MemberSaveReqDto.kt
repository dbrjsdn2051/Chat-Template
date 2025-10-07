package com.example.chatserver.controller.dto

data class MemberSaveReqDto (
    val name: String,
    val email: String,
    var password: String
)