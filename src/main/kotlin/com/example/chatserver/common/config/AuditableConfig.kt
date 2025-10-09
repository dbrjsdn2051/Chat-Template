package com.example.chatserver.common.config

import com.example.chatserver.common.`domain `.Auditable
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.toEntity
import org.springframework.stereotype.Component

@Component
class AuditableConfig {
    companion object {
        private var initialized = false
    }

    init {
        if (!initialized) {
            registerAuditHook()
            initialized = true
        }
    }

    private fun registerAuditHook() {
        EntityHook.subscribe { action ->
            val entity = action.toEntity<Long, LongEntity>()

            if (entity is Auditable) {
                when (action.changeType) {
                    EntityChangeType.Created -> {
                        entity.setCreatedTimestamp()
                    }

                    EntityChangeType.Updated -> {
                        entity.updateTimestamp()
                    }

                    else -> {}
                }
            }
        }
    }

}