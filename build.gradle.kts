import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.google.protobuf") version "0.9.4"
}

group = "uoykaii.ru"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.68.0"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") { }
                id("grpckt") { }
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/proto")
        }
    }
}

dependencies {
    // SPRING
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")

    // Kotlin Support
    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.17")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // S3
    implementation("io.minio:minio:8.5.13")

    // gRPC
    implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
    implementation(platform("io.grpc:grpc-bom:1.68.0"))
    implementation("io.grpc:grpc-api")
    implementation("io.grpc:grpc-core")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-netty-shaded")
    implementation("com.google.protobuf:protobuf-java-util:4.28.2")
    implementation("com.google.protobuf:protobuf-kotlin:4.28.2")

    // LOGGING
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

    // Doc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Jar> {
    destinationDirectory = File("/jarsDir")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
