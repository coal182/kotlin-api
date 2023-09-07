package com.codely.course.domain.course

interface CourseRepository {
    fun save(course: Course)
    fun getAll(): Array<Course>
}
