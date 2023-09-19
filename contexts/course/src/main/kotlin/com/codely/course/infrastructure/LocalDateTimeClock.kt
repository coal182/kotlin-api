package com.codely.course.infrastructure

import com.codely.shared.domain.Clock
import java.time.LocalDateTime

class LocalDateTimeClock : Clock {
    override fun now(): LocalDateTime = LocalDateTime.now()
}
