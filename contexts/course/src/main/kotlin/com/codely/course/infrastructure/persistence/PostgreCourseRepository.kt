package com.codely.course.infrastructure.persistence

import com.codely.course.domain.*
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet

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

    override fun find(id: CourseId): Result<Course> = runCatching {
        val query = "SELECT * FROM course where id=:id"
        val params = MapSqlParameterSource().addValue("id", id.value.toString())
        jdbcTemplate.queryForObject(query, params, mapRow())
    }.fold(
        onSuccess = {
            it?.let { Result.success(it) } ?: Result.failure(CourseNotFoundException(id))
        },
        onFailure = {
            Result.failure(it)
        }
    )

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
