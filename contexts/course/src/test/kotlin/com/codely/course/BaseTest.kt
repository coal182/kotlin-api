package com.codely.course

import com.codely.course.domain.Clock
import io.mockk.every
import io.mockk.unmockkAll
import java.time.LocalDateTime
import org.junit.jupiter.api.AfterEach

open class BaseTest {
    protected lateinit var clock: Clock
    @Suppress("SameParameterValue")
    protected fun givenFixedDate(fixedDatetime: LocalDateTime) {
        every { clock.now() } returns fixedDatetime
    }

    @AfterEach
    protected fun cleanMock() {
        unmockkAll()
    }
}