package com.codely.shared.acceptance

import com.codely.shared.database.PostgresTestUtils
import com.codely.shared.database.TestConfig
import io.mockk.unmockkAll
import io.restassured.RestAssured
import java.io.File
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig::class)
@Testcontainers
class BaseAcceptanceTest {
    @LocalServerPort
    private val springbootPort: Int = 0

    @Autowired
    private lateinit var postgresTestUtils: PostgresTestUtils

    companion object {
        private const val POSTGRES_PORT = 5432
        private const val KAFKA_PORT = 9092

        val environment: DockerComposeContainer<*> =
            DockerComposeContainer(File("docker-compose-test.yml"))
                .withOptions("--compatibility")
                .withExposedService("db", POSTGRES_PORT, Wait.forListeningPort())
                .withExposedService("kafka", KAFKA_PORT, Wait.forListeningPort())
                .withLocalCompose(true)

        @JvmStatic
        @BeforeAll
        fun start() {
            environment.start()
        }

        @JvmStatic
        @AfterAll
        fun stop() {
            environment.stop()
        }
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = springbootPort
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}
