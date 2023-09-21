package com.codely.course.infrastructure.rest

import com.codely.course.application.find.CourseFinder
import com.codely.course.domain.CourseCannotBeFoundError
import com.codely.course.domain.CourseNotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetFindCourseByIdController(private val courseFinder: CourseFinder) {

    @GetMapping("/course/{id}")
    fun execute(
        @PathVariable id: String
    ) = courseFinder.execute(id).fold(
        ifRight = {
            ResponseEntity.ok().body(it)
        },
        ifLeft = {
            when (it) {
                is CourseNotFoundError -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                is CourseCannotBeFoundError -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
        }
    )
}
