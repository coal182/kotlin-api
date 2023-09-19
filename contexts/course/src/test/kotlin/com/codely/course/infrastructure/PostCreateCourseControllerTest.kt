package com.codely.course.infrastructure

import com.codely.course.application.CourseCreator
import com.codely.course.domain.InvalidCourseDescriptionException
import com.codely.course.domain.InvalidCourseIdException
import com.codely.course.domain.InvalidCourseNameException
import com.codely.course.infrastructure.rest.CreateCourseRequest
import com.codely.course.infrastructure.rest.PostCreateCourseController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import kotlin.test.assertEquals

class PostCreateCourseControllerTest {
    private lateinit var courseCreator: CourseCreator
    private lateinit var controller: PostCreateCourseController

    @BeforeEach
    fun setUp(){
        courseCreator = mockk()
        controller = PostCreateCourseController(courseCreator)
    }

    @Test
    fun `should return a successful response`() {
        every { courseCreator.create(any(), any(), any()) } returns Unit

        val courseId = "03ef970b-719d-49c5-8d80-7dc762fe4be6"
        val response = controller.execute(CreateCourseRequest(courseId, "Test", "Description Test"))

        assertEquals(ResponseEntity.created(URI.create("/course/$courseId")).build(), response)

    }

    @Test
    fun `should fail when id is not valid`() {
        val invalidId = "invalid-id"

        every { courseCreator.create(any(), any(), any()) } throws InvalidCourseIdException(invalidId, null)

        val response = controller.execute(CreateCourseRequest(invalidId, "Test", "Description Test"))

        assertEquals(ResponseEntity.badRequest().body("The course id is not valid"), response)
    }

    @Test
    fun `should fail when name is not valid`() {
        val invalidName = ""

        every { courseCreator.create(any(), any(), any()) } throws InvalidCourseNameException(invalidName)

        val response = controller.execute(CreateCourseRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", invalidName, "Description Test 1"))

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The course name is not valid"), response)
    }

    @Test
    fun `should fail when description is not valid`() {
        val invalidDescription = ""

        every { courseCreator.create(any(), any(), any()) } throws InvalidCourseDescriptionException(invalidDescription)

        val response = controller.execute(CreateCourseRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Test 1", invalidDescription))

        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The course description is not valid"), response)
    }

    @Test
    fun `should fail when there is an unhandled exception`(){
        every { courseCreator.create(any(), any(), any()) } throws Throwable()

        val response = controller.execute(CreateCourseRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Test", "Description Test"))

        assertEquals(ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build(), response)
    }

}