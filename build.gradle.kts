plugins {
    kotlin("jvm") version "1.9.25"
//    kotlin("plugin.spring") version "1.9.25"
//    id("org.springframework.boot") version "3.4.4"
//    id("io.spring.dependency-management") version "1.1.7"
//    kotlin("plugin.jpa") version "1.9.25"
}


subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        /* kotlin */
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        /* lombok */
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        /* junit */
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

allprojects {
    group = "org.aing"
    version = "0.0.1-SNAPSHOT"


    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    repositories {
        mavenCentral()
    }
}

//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    runtimeOnly("io.micrometer:micrometer-registry-prometheus")