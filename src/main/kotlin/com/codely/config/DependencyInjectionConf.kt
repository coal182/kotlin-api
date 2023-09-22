package com.codely.config

import com.codely.common.domain.Publisher
import com.codely.course.application.CourseCreator
import com.codely.course.application.find.CourseFinder
import com.codely.course.domain.CourseRepository
import com.codely.course.infrastructure.LocalDateTimeClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DependencyInjectionConf {

    @Bean
    fun clock() = LocalDateTimeClock()

    @Bean
    fun courseCreator(courseRepository: CourseRepository, publisher: Publisher) = CourseCreator(courseRepository, publisher)

    @Bean
    fun courseFinder(courseRepository: CourseRepository) = CourseFinder(courseRepository)
}
