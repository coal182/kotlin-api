package com.codely.config

import com.codely.course.application.CourseCreator
import com.codely.course.application.find.CourseFinder
import com.codely.shared.domain.Clock
import com.codely.course.domain.CourseRepository
import com.codely.course.infrastructure.LocalDateTimeClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DependencyInjectionConf {

    @Bean
    fun clock() = LocalDateTimeClock()

    @Bean
    fun courseCreator(courseRepository: CourseRepository, clock: Clock) = CourseCreator(courseRepository, clock)

    @Bean
    fun courseFinder(courseRepository: CourseRepository) = CourseFinder(courseRepository)
}
