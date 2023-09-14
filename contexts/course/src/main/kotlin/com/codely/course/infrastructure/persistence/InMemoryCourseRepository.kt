package com.codely.course.infrastructure.persistence

import com.codely.course.domain.course.Course
import com.codely.course.domain.course.CourseId
import com.codely.course.domain.course.CourseRepository

class DatabaseConnectionData(var username: String = "", var password: String = "")
class InMemoryCourseRepository(private val connectionData: DatabaseConnectionData) : CourseRepository {
    val repository: MutableList<Course> = mutableListOf<Course>()

    init {
        println()
    }

    override fun save(course: Course) {
        repository.add(course)
    }

    override fun find(id: CourseId) = repository.toTypedArray()[0]
}
