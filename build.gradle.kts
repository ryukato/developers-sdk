import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    buildscript {
        repositories {
            mavenLocal()
            jcenter()
            mavenCentral()
        }
    }

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

plugins {
    base
    java
    kotlin("jvm") version Versions.kotlin apply false
    id("com.jfrog.bintray") version "1.8.5"
    `maven-publish`
    signing
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
