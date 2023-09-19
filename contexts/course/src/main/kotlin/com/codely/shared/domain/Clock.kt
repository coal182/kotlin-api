package com.codely.shared.domain

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}
