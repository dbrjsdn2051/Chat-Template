package com.example.chatserver.chat.domain.repository

import com.example.chatserver.chat.domain.CreateReadStatus
import org.springframework.stereotype.Repository

@Repository
interface ReadStatusRepository {
    fun save(createReadStatus: CreateReadStatus): Long
}