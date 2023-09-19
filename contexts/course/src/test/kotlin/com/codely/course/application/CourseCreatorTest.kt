package com.codely.course.application

import com.codely.course.BaseTest
import com.codely.course.domain.Course
import com.codely.course.domain.CourseRepository
import com.codely.course.domain.InvalidCourseDescriptionException
import com.codely.course.domain.InvalidCourseIdException
import com.codely.course.domain.InvalidCourseNameException
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CourseCreatorTest : BaseTest() {
    private lateinit var courseRepository: CourseRepository
    private lateinit var courseCreator: CourseCreator

    @BeforeEach
    fun setUp() {
        courseRepository = mockk(relaxUnitFun = true)
        clock = mockk()
        courseCreator = CourseCreator(courseRepository, clock)
    }

    @Test
    fun `should create a course successfully`() {
        givenFixedDate(fixedDate)
        courseCreator.create(id, name, description)
        thenTheCourseShouldBeSaved()
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
                Course.from(
                    id = id,
                    name = name,
                    description = description,
                    createdAt = fixedDate
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
    }
}
