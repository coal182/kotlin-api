package com.codely.course.application

import com.codely.common.domain.Publisher
import com.codely.course.domain.Course
import com.codely.course.domain.CourseDescription
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseName
import com.codely.course.domain.CourseRepository

class CourseCreator(private val repository: CourseRepository, private val publisher: Publisher) {
    fun create(id: String, name: String, description: String) {
        Course.create(CourseId.fromString(id), CourseName(name), CourseDescription(description)).let {
            repository.save(it)
            publisher.publish(it.events)
        }
    }
}
