package com.codely.api.infrastructure

import com.codely.course.application.CourseCreator
import com.codely.course.domain.course.Course
import com.codely.course.domain.course.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HolaController(
    @Autowired val courseCreator: CourseCreator,
    @Autowired val courseRepository: CourseRepository,
) {

    @GetMapping("/hola")
    @ResponseBody
    fun execute(): ResponseEntity<Course> {
        courseCreator.create("220fcd5f-6aa9-4233-afdb-44fb18bdc506", "Prueba Curso", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque erat, posuere ut posuere sit amet, congue eu sapien. Nulla consequat vivamus.")
        val savedCourses = courseRepository.getAll()
        return ResponseEntity.ok(savedCourses[0])
    }
}
