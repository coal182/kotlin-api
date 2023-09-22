package com.codely.course.application

import com.codely.common.domain.Publisher
import com.codely.course.BaseTest
import com.codely.course.domain.Course
import com.codely.course.domain.CourseCreated
import com.codely.course.domain.CourseDescription
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseName
import com.codely.course.domain.CourseRepository
import com.codely.course.domain.InvalidCourseDescriptionException
import com.codely.course.domain.InvalidCourseIdException
import com.codely.course.domain.InvalidCourseNameException
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CourseCreatorTest : BaseTest() {
    private lateinit var courseRepository: CourseRepository
    private lateinit var courseCreator: CourseCreator
    private lateinit var publisher: Publisher

    @BeforeEach
    fun setUp() {
        courseRepository = mockk(relaxUnitFun = true)
        publisher = mockk(relaxUnitFun = true)
        courseCreator = CourseCreator(courseRepository, publisher)
    }

    @Test
    fun `should create a course successfully`() {
        givenFixedDate(fixedDate)

        courseCreator.create(id, name, description)

        thenTheCourseShouldBeSaved()
        `then the event should be published`()
    }

    private fun `then the event should be published`() {
        verify { publisher.publish(listOf(event)) }
    }

    @Test
    fun `should fails with invalid id`() {
        givenFixedDate(fixedDate)
        assertThrows<InvalidCourseIdException> { courseCreator.create("Invalid", name, description) }
    }

    @Test
    fun `should fail with invalid name`() {
        givenFixedDate(fixedDate)
        assertThrows<InvalidCourseNameException> { courseCreator.create(id, "    ", description) }
    }

    @Test
    fun `should fail with invalid description`() {
        givenFixedDate(fixedDate)
        assertThrows<InvalidCourseDescriptionException> { courseCreator.create(id, name, greaterThan150Description) }
    }

    private fun thenTheCourseShouldBeSaved() {
        verify {
            courseRepository.save(
                Course(
                    id = CourseId(UUID.fromString(id)),
                    name = CourseName(name),
                    description = CourseDescription(description),
                    createdAt = fixedDate,
                    events = listOf(event)
                )
            )
        }
    }

    companion object {
        private const val id = "caebae03-3ee9-4aef-b041-21a400fa1bb7"
        private const val name = "Kotlin Hexagonal Architecture Api Course"
        private const val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque erat, posuere ut posuere sit amet, congue eu sapien. Nulla consequat vivamus."
        private const val greaterThan150Description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque erat, posuere ut posuere sit amet, congue eu sapien. Nulla consequat vivamus.150"
        private val fixedDate = LocalDateTime.parse("2022-08-09T14:50:42")
        private val event = CourseCreated(
            courseId = CourseId(UUID.fromString(id)),
            courseName = CourseName(name),
            courseDescription = CourseDescription(description),
            createdAt = fixedDate
        )
    }
}
