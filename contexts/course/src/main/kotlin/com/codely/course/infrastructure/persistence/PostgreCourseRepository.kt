package com.codely.course.infrastructure.persistence

import com.codely.common.Either
import com.codely.common.Left
import com.codely.common.Right
import com.codely.course.domain.Course
import com.codely.course.domain.CourseError
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseNotFoundError
import com.codely.course.domain.CourseRepository
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgreCourseRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : CourseRepository {
    override fun save(course: Course) {
        MapSqlParameterSource()
            .addValue("id", course.id.value.toString())
            .addValue("name", course.name.value)
            .addValue("description", course.description.value)
            .addValue("createdAt", course.createdAt)
            .let { params ->
                jdbcTemplate.update(
                    "INSERT INTO course (id, name, description, created_at) VALUES (:id,:name,:description,:createdAt)",
                    params
                )
            }
    }

    override fun find(id: CourseId): Either<CourseError, Course> = try {
        val query = "SELECT * FROM course where id=:id"
        val params = MapSqlParameterSource().addValue("id", id.value.toString())
        jdbcTemplate.queryForObject(query, params, mapRow())
            ?.let { Right(it) }
            ?: Left(CourseNotFoundError(id))
    } catch (exception: Throwable) {
            Left(CourseNotFoundError(id))
    }

    private fun mapRow(): RowMapper<Course> {
        return RowMapper { rs: ResultSet, _: Int ->
            val id = rs.getString("id")
            val name = rs.getString("name")
            val description = rs.getString("description")
            val createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            Course.from(id, name, description, createdAt)
        }
    }
}
