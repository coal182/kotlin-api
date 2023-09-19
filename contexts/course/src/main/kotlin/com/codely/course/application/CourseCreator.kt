package com.codely.course.application

import com.codely.shared.domain.Clock
import com.codely.course.domain.Course
import com.codely.course.domain.CourseRepository

class CourseCreator(private val repository: CourseRepository, private val clock: Clock) {
    fun create(id: String, name: String, description: String) {
        Course.from(id, name, description, clock.now()).let {
            repository.save(it)
        }
    }
}
