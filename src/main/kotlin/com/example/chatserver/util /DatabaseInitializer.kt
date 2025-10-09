package com.example.chatserver.`util `

import com.example.chatserver.chat.domain.ChatMessages
import com.example.chatserver.chat.domain.ChatParticipants
import com.example.chatserver.chat.domain.ChatRooms
import com.example.chatserver.domain.Members
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    private val database: Database
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(Members, ChatRooms, ChatParticipants, ChatMessages)
        }
    }
}
