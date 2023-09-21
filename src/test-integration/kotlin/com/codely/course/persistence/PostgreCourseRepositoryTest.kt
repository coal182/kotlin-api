package com.codely.course.persistence

import com.codely.common.course.CourseMother
import com.codely.course.domain.CourseId
import com.codely.course.infrastructure.persistence.PostgreCourseRepository
import com.codely.shared.persistence.BaseIntegrationTest
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostgreCourseRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var repository: PostgreCourseRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `should save a course`() {
        repository.save(courseToSave)
    }

    @Test
    fun `should find a course succesfully `() {
        repository.save(courseToSave)

        val courseFromDb = repository.find(CourseId.fromString(courseId))

        assertEquals(Result.success(courseToSave), courseFromDb)
    }

    companion object {
        const val courseId = "13590efb-c181-4c5f-9f95-b768abde13e2"
        private val courseToSave = CourseMother.sample(courseId, "Test", "Test Description", LocalDateTime.of(2023, 1, 1, 0, 0))
    }
}
