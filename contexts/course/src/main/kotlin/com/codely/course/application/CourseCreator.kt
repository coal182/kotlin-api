package com.codely.course.application

import com.codely.course.domain.Clock
import com.codely.course.domain.course.Course
import com.codely.course.domain.course.CourseRepository

class CourseCreator(private val repository: CourseRepository, private val clock: Clock) {
    fun create(id: String, name: String, description: String) {
        Course.from(id, name, description, clock.now()).let {
            repository.save(it)
        }
    }
}