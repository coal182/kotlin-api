package com.codely.course.domain

import com.codely.common.domain.DomainEvent
import java.time.LocalDateTime

data class CourseCreated(
    val courseId: CourseId,
    val courseName: CourseName,
    val courseDescription: CourseDescription,
    val createdAt: LocalDateTime
) : DomainEvent(
    type = "CourseCreated",
    payload = """
        {
        "id": ${courseId.value},
        "name": ${courseName.value},
        "description": ${courseDescription.value},
        "created_at": $createdAt
        }
    """.trimIndent()
)
