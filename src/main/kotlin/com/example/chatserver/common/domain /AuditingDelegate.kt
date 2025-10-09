package com.example.chatserver.common.`domain `

import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.dao.id.LongIdTable
import java.time.LocalDateTime


interface Auditable {
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime

    fun setCreatedTimestamp() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    fun updateTimestamp() {
        updatedAt = LocalDateTime.now()
    }
}

abstract class BaseTable(name: String) : LongIdTable(name) {
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}


