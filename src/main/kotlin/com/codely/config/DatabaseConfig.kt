package com.codely.config

import com.codely.course.infrastructure.persistence.DatabaseConnectionData
import com.codely.course.infrastructure.persistence.PostgreCourseRepository
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "database.connection")
    fun databaseConnectionData() = DatabaseConnectionData()

    @Bean
    fun courseRepository(jdbcTemplate: NamedParameterJdbcTemplate) = PostgreCourseRepository(jdbcTemplate)
}
