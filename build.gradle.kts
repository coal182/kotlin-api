import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.diffplug.spotless") version "5.7.0"
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
    id("org.flywaydb.flyway") version "9.1.6"
    application
}

group = "com.codely"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

sourceSets {
    create("test-integration") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val testIntegrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["test-integration"].output.classesDirs
    classpath = sourceSets["test-integration"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter("test")
}

val testcontainersVersion = "1.17.3"

dependencies {
    // internal dependencies
    implementation(project(":contexts:course"))
    implementation(project(":common"))

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.9.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("javax.inject:javax.inject:1")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core:8.5.13")
    implementation("com.h2database:h2")
    implementation("org.postgresql:postgresql:42.4.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.apache.kafka:kafka-clients:3.3.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.12.7")
    testIntegrationImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testIntegrationImplementation("org.testcontainers:jdbc:$testcontainersVersion")
    testIntegrationImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testIntegrationImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testIntegrationImplementation("org.testcontainers:kafka:$testcontainersVersion")
    // rest-assured 4.5.1 por problemas de compatibilidad https://github.com/rest-assured/rest-assured/issues/1612
    testIntegrationImplementation("io.rest-assured:rest-assured:4.5.1")
    testIntegrationImplementation("io.rest-assured:kotlin-extensions:4.5.1")
    testIntegrationImplementation(project(":common-test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    kotlin {
        ktlint()
            .userData(
                mapOf(
                    "insert_final_newline" to "true"
                )
            )
    }
    kotlinGradle {
        ktlint()
    }
}

tasks.check {
    // it fails deploying test database twice in git actions
    // dependsOn(integrationTest)
    dependsOn(tasks.spotlessCheck)
}

flyway {
    val host = System.getenv("POSTGRE_URL") ?: "localhost"
    val port = "5432"
    cleanDisabled = false
    url = "jdbc:postgresql://$host:$port/course_database"
    user = System.getenv("POSTGRE_USERNAME") ?: "course_username"
    password = System.getenv("POSTGRE_PASSWORD") ?: "course_password"
}

tasks.bootJar {
    launchScript()
}
