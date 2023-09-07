package com.codely.course.infrastructure.persistence

import com.codely.course.domain.course.Course
import com.codely.course.domain.course.CourseRepository
class InMemoryCourseRepository() : CourseRepository {

    val repository: MutableList<Course> = mutableListOf<Course>()
    override fun save(course: Course) {
        repository.add(course)
    }

    override fun getAll() = repository.toTypedArray()
}
