package com.codely.common.domain

interface Publisher {
    fun publish(events: List<DomainEvent>)
    fun get(): List<DomainEvent>
    fun flush()
}
