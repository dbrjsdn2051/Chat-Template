package com.example.chatserver.domain

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable


enum class Role {
    ADMIN, USER
}

object Members : LongIdTable("members") {
    val name = varchar("name", 50)
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    val role = enumerationByName("role", 20, Role::class).default(Role.USER)
}

class Member(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Member>(Members)

    var name by Members.name
    var email by Members.email
    var password by Members.password
    var role by Members.role
}


