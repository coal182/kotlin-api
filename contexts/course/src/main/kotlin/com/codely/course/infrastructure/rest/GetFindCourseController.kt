package com.codely.course.infrastructure.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetFindCourseController() {

    @GetMapping("/course/{id}")
    fun execute(
        @PathVariable id: String
    ): ResponseEntity<String> {
        return try {
            ResponseEntity.ok().body("""
                {
                    "id": "97fa5af4-bd81-45d5-974f-d5a3970af252",
                    "name": "Saved course",
                    "description": "Saved course description",
                    "created_at": "2023-08-31T09:07:36"
                }
            """.trimIndent())
        } catch (exception: Throwable){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}