package com.codely.course.infrastructure.persistence

import com.codely.common.Either
import com.codely.common.Right
import com.codely.course.domain.Course
import com.codely.course.domain.CourseError
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseRepository

class DatabaseConnectionData(var username: String = "", var password: String = "")
class InMemoryCourseRepository(private val connectionData: DatabaseConnectionData) : CourseRepository {
    val repository: MutableList<Course> = mutableListOf<Course>()

    init {
        println()
    }

    override fun save(course: Course) {
        repository.add(course)
    }

    override fun find(id: CourseId): Either<CourseError, Course> = Right(repository.toTypedArray()[0])
}
