package com.codely.common.infrastructure

import com.codely.common.domain.DomainEvent
import com.codely.common.domain.Publisher

class InMemoryPublisher : Publisher {
    private val events = mutableListOf<DomainEvent>()
    override fun publish(data: List<DomainEvent>) {
        events += data
    }

    override fun get(): List<DomainEvent> {
        return events
    }

    override fun flush() {
        events.clear()
    }
}
