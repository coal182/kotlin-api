package com.codely.course.infrastructure

import com.codely.common.Left
import com.codely.common.Right
import com.codely.course.application.find.CourseFinder
import com.codely.course.application.find.CourseResponse
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseNotFoundError
import com.codely.course.infrastructure.rest.GetFindCourseByIdController
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class GetFindCourseByIdControllerTest {
    private lateinit var courseFinder: CourseFinder
    private lateinit var controller: GetFindCourseByIdController

    @BeforeEach
    fun setUp() {
        courseFinder = mockk()
        controller = GetFindCourseByIdController(courseFinder)
    }

    @Test
    fun `should return the course response`() {
        `given a course response`()

        val response = `when a course is requested by id`()

        `then a successful response is returned`(response)
    }

    private fun `then a successful response is returned`(actualResponse: ResponseEntity<CourseResponse>) {
        assertEquals(ResponseEntity(course, HttpStatus.OK), actualResponse)
    }

    private fun `when a course is requested by id`() = controller.execute(courseId)

    private fun `given a course response`() {
        every { courseFinder.execute(any()) } returns Right(course)
    }

    @Test
    fun `should fail when course is not found`() {
        `given there is no course found`()

        val response = `when a course is requested by id`()

        `then a not found response is returned`(response)
    }

    private fun `then a not found response is returned`(actualResponse: ResponseEntity<CourseResponse>) {
        assertEquals(
            ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
            actualResponse
        )
    }

    private fun `given there is no course found`() {
        every { courseFinder.execute(any()) } returns Left(
            CourseNotFoundError(CourseId.fromString(courseId))
        )
    }

    companion object {
        private const val courseId = "e90cadc2-fbf6-49ee-bca4-3fc652ea0134"
        private val course = CourseResponse(
            id = courseId,
            name = "Kotlin - Your first API",
            description = "Kotlin - Your first API",
            createdAt = LocalDateTime.parse("2022-08-31T09:07:36")
        )
    }
}
