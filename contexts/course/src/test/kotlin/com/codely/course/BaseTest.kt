package com.codely.course

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.time.LocalDateTime
import org.junit.jupiter.api.AfterEach

open class BaseTest {
    @Suppress("SameParameterValue")
    protected fun givenFixedDate(fixedDatetime: LocalDateTime) {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns fixedDatetime
    }

    @AfterEach
    protected fun cleanMock() {
        unmockkAll()
    }
}
