package com.codely.course.persistence

import com.codely.course.domain.course.Course
import com.codely.course.infrastructure.persistence.PostgreCourseRepository
import java.sql.ResultSet
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import com.codely.shared.persistence.BaseIntegrationTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostgreCourseRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var repository: PostgreCourseRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `should save a course`() {
        val courseId = "13590efb-c181-4c5f-9f95-b768abde13e2"
        val courseToSave = Course.from(courseId, "Test", "Test Description", LocalDateTime.of(2023, 1, 1, 0, 0))
        repository.save(courseToSave)

        val courseFromDb = jdbcTemplate.queryForObject(
            "select * from course where id=?",
            mapRow(),
            courseId
        )

        assertEquals(courseToSave, courseFromDb)
    }

    private fun mapRow(): RowMapper<Course> {
        return RowMapper { rs: ResultSet, _: Int ->
            val createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            Course.from(rs.getString("id"), rs.getString("name"), rs.getString("description"), createdAt)
        }
    }
}
