package com.codely.common.domain

import java.util.UUID

sealed class DomainEventError(message: String) : Error()
data class DomainEventDeserializationError(val id: UUID) : DomainEventError("Message cannot be deserialized: structure not valid on broker message with id <$id>")
