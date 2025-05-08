plugins {
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("com.google.devtools.ksp")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.openfeign.querydsl:querydsl-core:6.11")
    ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:6.11")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:6.11")
    runtimeOnly("com.mysql:mysql-connector-j")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.getByName("bootJar") {
    enabled = false
}
