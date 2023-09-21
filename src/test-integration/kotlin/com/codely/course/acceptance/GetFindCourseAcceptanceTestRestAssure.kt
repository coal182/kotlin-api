package com.codely.course.acceptance

import com.codely.common.course.CourseMother
import com.codely.course.infrastructure.persistence.PostgreCourseRepository
import com.codely.shared.acceptance.BaseAcceptanceTest
import com.codely.shared.acceptance.isEqualToJson
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class GetFindCourseAcceptanceTestRestAssure : BaseAcceptanceTest() {

    @Autowired
    private lateinit var courseRepository: PostgreCourseRepository

    @Test
    @Sql("classpath:db/fixtures/find/add-course-data.sql")
    fun `should find course successfully with fixture`() {
        When {
            get("/course/${course.id.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            body().asString().isEqualToJson(expectedCourseResponse)
        }
    }

    @Test
    fun `should find course successfully with course creation`() {
        val response = Given {
            `an existent course`()
            contentType(ContentType.JSON)
            body("")
        } When {
            get("/course/${course.id.value}")
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            body().asString()
        }
        response.isEqualToJson(expectedCourseResponse)
    }

    private fun `an existent course`() {
        courseRepository.save(course)
    }

    companion object {
        private val now = LocalDateTime.parse("2023-08-31T09:07:36")
        private val course = CourseMother.sample(
            "97fa5af4-bd81-45d5-974f-d5a3970af252",
            "Saved course",
            "Saved course description",
            now
        )
        private val expectedCourseResponse = """
                {
                    "id": "${course.id.value}",
                    "name": "${course.name.value}",
                    "description": "${course.description.value}",
                    "createdAt": "${course.createdAt}"
                }
            """.trimIndent()
    }
}
