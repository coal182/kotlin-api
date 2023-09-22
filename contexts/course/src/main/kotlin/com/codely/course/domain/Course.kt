package com.codely.course.domain

import com.codely.common.domain.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class CourseId(val value: UUID) {
    companion object {
        fun fromString(id: String) = try {
            CourseId(UUID.fromString(id))
        } catch (exception: Exception) {
            throw InvalidCourseIdException(id, exception)
        }
    }
}

data class CourseName(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.isEmpty() || value.isBlank()) {
            throw InvalidCourseNameException(value)
        }
    }
}

data class CourseDescription(val value: String) {
    init {
        validate()
    }

    private fun validate() {
        if (value.isEmpty() || value.isBlank() || value.length > 150) {
            throw InvalidCourseDescriptionException(value)
        }
    }
}
data class Course(
    val id: CourseId,
    val name: CourseName,
    val description: CourseDescription,
    val createdAt: LocalDateTime,
    val events: List<DomainEvent>
) {
    companion object {
        fun create(
            id: CourseId,
            name: CourseName,
            description: CourseDescription
        ) = LocalDateTime.now().let { createdAt ->
            Course(id, name, description, createdAt, listOf(CourseCreated(id, name, description, createdAt)))
        }

        fun from(id: String, name: String, description: String, createdAt: LocalDateTime) =
            Course(
                CourseId.fromString(id),
                CourseName(name),
                CourseDescription(description),
                createdAt,
                emptyList()
            )
    }
}
