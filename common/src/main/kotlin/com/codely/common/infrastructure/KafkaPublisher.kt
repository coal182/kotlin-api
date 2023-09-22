package com.codely.common.infrastructure

import com.codely.common.domain.DomainEvent
import com.codely.common.domain.Publisher
import java.time.Duration
import java.util.Properties
import java.util.UUID
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

class PublisherKafkaConfig(var bootstrapServers: String = "", var topic: String = "")

class KafkaPublisher(
    private val config: PublisherKafkaConfig
) : Publisher {
    private val producer: Producer<String, String>
    private val consumer: Consumer<String, String>

    init {
        // Configuring the consumer
        val producerProps = Properties()
        producerProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = config.bootstrapServers
        producerProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        producerProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        producer = KafkaProducer(producerProps)

        // Configuring the consumer
        val consumerProps = Properties()
        consumerProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = config.bootstrapServers
        consumerProps[ConsumerConfig.GROUP_ID_CONFIG] = "your-consumer-group-id"
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        consumer = KafkaConsumer(consumerProps)
        consumer.subscribe(listOf(config.topic))
    }
    override fun publish(events: List<DomainEvent>) {
        events.forEach { event ->
            val record = ProducerRecord(config.topic, event.getId(), event.toJson())
            producer.send(record)
        }
    }

    override fun get(): List<DomainEvent> {
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofMillis(100))
        val events: MutableList<DomainEvent> = mutableListOf()

        for (record in records) {
            val eventId = UUID.fromString(record.key())
            val payload = record.value()
            val event = DomainEvent.fromJson(eventId, payload)
            events.add(event)
        }

        return events
    }

    override fun flush() {
        TODO("Not yet implemented")
    }
}
