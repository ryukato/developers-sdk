@file:Suppress("UnstableApiUsage")

import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublicationContainer
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.signing
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugins.signing.SigningExtension

object Versions {
    const val kotlin = "1.4.0"
    const val coroutines = "1.3.9"
    const val coroutinesCoreJs = "1.3.3"
    const val ktor = "1.3.0"
    const val apacheCommon = "3.10"
    const val commonCodec = "1.14"
    const val junit = "5.6.2"
    const val springBoot = "2.3.4.RELEASE"
    const val springDependencyManagement = "1.0.10.RELEASE"
    const val springPlugin = "1.3.72"
}

object Libs {
    // kotlin
    const val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinxCoroutinesCoreJs = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.coroutinesCoreJs}"
    const val kotlinxCoroutinesReactor =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.coroutines}"

    // ktor
    const val ktorClient = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorClientCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktorClientJackson = "io.ktor:ktor-client-jackson:${Versions.ktor}"
    const val ktorClientLoggingJvm = "io.ktor:ktor-client-logging-jvm:${Versions.ktor}"
    // ktor-test
    const val ktorClientMock = "io.ktor:ktor-client-mock:${Versions.ktor}"
    const val ktorClientMockJvm = "io.ktor:ktor-client-mock-jvm:${Versions.ktor}"
    const val ktorClientMockJs = "io.ktor:ktor-client-mock-js:${Versions.ktor}"
    const val ktorClientMockNative = "io.ktor:ktor-client-mock-native:${Versions.ktor}"

    //apache-common
    const val commonsLang = "org.apache.commons:commons-lang3:${Versions.apacheCommon}"

    const val commonCodec = "commons-codec:commons-codec:${Versions.commonCodec}"

    //test-junit
    const val junit = "org.junit.jupiter:junit-jupiter:${Versions.junit}"

    //spring
    const val springBootDependencies = "org.springframework.boot:spring-boot-dependencies:${Versions.springBoot}"
    const val springBoot = "org.springframework.boot:spring-boot:${Versions.springBoot}"
    const val springBootConfigurationProcessor = "org.springframework.boot:spring-boot-configuration-processor:${Versions.springBoot}"
    const val springBootStarterWebFlux = "org.springframework.boot:spring-boot-starter-webflux:${Versions.springBoot}"
    const val springBootAutoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${Versions.springBoot}"

}

fun PluginDependenciesSpec.kotlin() {
    kotlin("jvm")
    kotlin("kapt")
}

fun PluginDependenciesSpec.kotlinSpringBoot() {
    kotlin()
    id("org.springframework.boot") version Versions.springBoot
    id("io.spring.dependency-management") version Versions.springDependencyManagement
    kotlin("plugin.spring") version Versions.springPlugin
}

fun PluginDependenciesSpec.publish() {
    id("com.jfrog.bintray")
    `maven-publish`
    signing
}

fun Project.kotlinDependencies() {
    dependencies {
        "implementation"(Libs.kotlinStdlibJdk8)
        "implementation"(Libs.kotlinReflect)
    }
}

fun Project.ktorDependencies() {
    dependencies {
        "implementation"(Libs.ktorClient)
        "implementation"(Libs.ktorClientCio)
        "implementation"(Libs.ktorClientJackson)
        "implementation"(Libs.ktorClientLoggingJvm)
        "implementation"(Libs.commonsLang)
        "implementation"(Libs.commonCodec)
        "testImplementation"(Libs.ktorClientMock)
        "testImplementation"(Libs.ktorClientMockJvm)
        "testImplementation"(Libs.ktorClientMockJs)
        "testImplementation"(Libs.ktorClientMockNative)
    }
}

fun Project.junitDependencies() {
    dependencies {
        "testImplementation"(Libs.junit) {
            exclude(group = "org.junit.vintage")
        }
    }
}

fun Project.springBootDependencies() {
    dependencies {
        "kapt"("org.springframework.boot:spring-boot-configuration-processor:${Versions.springBoot}")
        "testImplementation"("org.springframework.boot:spring-boot-starter-webflux:${Versions.springBoot}")
    }
}

fun Project.kotlinCoroutineDependencies(requiresReactor: Boolean = true) {
    dependencies {
        "implementation"(Libs.kotlinxCoroutinesCore)
        "implementation"(Libs.kotlinxCoroutinesCoreJs)
        "testImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")

        // if (requiresReactor) {
        //     "implementation"(Libs.kotlinxCoroutinesReactor)
        //     "testImplementation"("io.projectreactor:reactor-test:${Versions.reactor}")
        // }
    }
}
