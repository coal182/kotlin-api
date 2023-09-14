package com.codely.common.course

import com.codely.course.domain.course.Course
import java.time.LocalDateTime

object CourseMother {

    fun sample(
        id: String = "ce30c1ad-bcf9-47f3-9228-fca9ab081f57",
        name: String = "Course Mother Name",
        description: String = "Course Mother Description",
        createdAt: LocalDateTime = LocalDateTime.parse("2023-08-31T09:00:00")
    ) = Course.from(id, name, description, createdAt)
}