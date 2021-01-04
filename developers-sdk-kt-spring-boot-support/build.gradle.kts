@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	maven
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}


group = projectGroupId
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

buildscript {
	repositories {
		mavenLocal()
		mavenCentral()
		jcenter()
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation(project(":developers-sdk-kt"))

	// ktor
	implementation("io.ktor:ktor-client-core:1.3.0")
	implementation("io.ktor:ktor-client-cio:1.3.0")
	implementation("io.ktor:ktor-client-jackson:1.3.0")
	implementation("io.ktor:ktor-client-logging-jvm:1.3.0")
	// kotlin logging
	implementation("io.github.microutils:kotlin-logging:1.7.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<Test> {
	useJUnitPlatform()
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

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	enabled = false
}

tasks.withType<Jar> {
	enabled = true
}

