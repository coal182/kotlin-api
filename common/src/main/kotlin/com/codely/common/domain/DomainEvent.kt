package com.codely.common.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.util.UUID

open class DomainEvent(val type: String, val payload: String) {
    private val id = UUID.randomUUID()
    private val occurredOn = LocalDateTime.now()

    fun getId(): String {
        return id.toString()
    }

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromJson(eventId: UUID, event: String): DomainEvent {
            val gson = Gson()
            val jsonObject = gson.fromJson(event, JsonObject::class.java)

            if (jsonObject != null && jsonObject.has("type") && jsonObject.has("payload")) {
                val type = jsonObject.get("type").asString
                val payload = jsonObject.get("payload").asString

                // Perform further validation if necessary

                return DomainEvent(type, payload)
            } else {
                throw DomainEventDeserializationError(eventId)
            }
        }
    }
}
