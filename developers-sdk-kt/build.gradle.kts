import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	maven
	kotlin("jvm") version "1.3.72"
	id("com.jfrog.bintray") version "1.7.3"
	`maven-publish`
}

group = "com.yyoo"
version = "0.0.1-SNAPSHOT"
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
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

	// ktor
	implementation("io.ktor:ktor-client-core:1.3.0")
	implementation("io.ktor:ktor-client-cio:1.3.0")
	implementation("io.ktor:ktor-client-jackson:1.3.0")
	implementation("io.ktor:ktor-client-logging-jvm:1.3.0")
	implementation("org.apache.commons:commons-lang3:3.10")
	implementation("commons-codec:commons-codec:1.14")

	// bouncy-castle
	implementation("org.bouncycastle:bcprov-jdk15on:1.64")
	// kotlin logging
	implementation("io.github.microutils:kotlin-logging:1.7.10")

	// logging
	implementation("ch.qos.logback:logback-core:1.2.3")
	implementation("ch.qos.logback:logback-classic:1.2.3")

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.2") {
		exclude("org.junit.vintage")
	}

	testImplementation("io.ktor:ktor-client-mock:1.3.0")
	testImplementation("io.ktor:ktor-client-mock-jvm:1.3.0")
	testImplementation("io.ktor:ktor-client-mock-js:1.3.0")
	testImplementation("io.ktor:ktor-client-mock-native:1.3.0")
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
