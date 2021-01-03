@file:Suppress("UnstableApiUsage")

import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    maven
    kotlin("jvm") version "1.3.72"
    id("com.jfrog.bintray") version "1.8.5"
    `maven-publish`
    signing
}

group = projectGroupId
version = "0.0.2"
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

val publishVersion = version as String

publishing {
    publications {
        create<MavenPublication>("mavenSDK") {
            groupId = publishGroupId
            artifactId = publishArtifactId
            version = publishVersion

            from(components["java"])

            pom {
                name.set(publishName)
                description.set(publishDescription)
                url.set(publishProjectUrl)
                packaging = "jar"

                licenses {
                    license {
                        name.set(publishLicense)
                    }
                }
                developers {
                    developerList.forEach {
                        developer {
                            id.set(it["id"])
                            name.set(it["name"])
                            email.set(it["email"])
                        }
                    }
                }
                scm {
                    connection.set(scmConnectionUrl)
                    developerConnection.set(developerConnectionUrl)
                    url.set(gitRepositoryUrl)
                }
            }

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }

    repositories {
        maven {
            url = releaseTargetRepoUrl(version.toString())
            credentials {
                username = getProperty("MAVEN_USERNAME", project)
                password = getProperty("MAVEN_PASSWORD", project)
            }
        }
    }
}
java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    val signingKey = getProperty("GPG_SIGNING_KEY", project)
    if (signingKey != null) {
        val signingPass = getProperty("GPG_SIGNING_PASSWORD", project)
       useInMemoryPgpKeys(signingKey, signingPass)
    } else {
        useGpgCmd()
    }

    sign(publishing.publications["mavenSDK"])
}

bintray {
    user = getProperty("BINTRAY_USERNAME", project)
    key = getProperty("BINTRAY_KEY", project)
    setPublications("mavenSDK")

    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = publishGroupId
        name = publishArtifactId
        userOrg = bintrayUserOrg
        setLicenses("MIT")
        vcsUrl = gitRepositoryUrl
    })
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

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks.withType<com.jfrog.bintray.gradle.tasks.BintrayUploadTask> {
    doFirst {
        publishing.publications
            .filterIsInstance<MavenPublication>()
            .forEach { publication ->
                val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                if (moduleFile.exists()) {
                    publication.artifact(object : FileBasedMavenArtifact(moduleFile) {
                        override fun getDefaultExtension() = "module"
                    })
                }
            }
    }
}
