package com.codely.config

import com.codely.common.infrastructure.KafkaPublisher
import com.codely.common.infrastructure.PublisherKafkaConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PublisherConfig {
    @Bean
    @ConfigurationProperties(prefix = "publisher")
    fun publisherConfigData() = PublisherKafkaConfig()

    @Bean
    fun publisher(publisherConfigData: PublisherKafkaConfig) = KafkaPublisher(publisherConfigData)
}
