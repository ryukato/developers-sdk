
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    java
    maven
    kotlin("jvm") version "1.3.72"
    id("com.jfrog.bintray") version "1.8.5"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.20"
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

val publishGroupId = "com.github.ryukato"
val publishArtifactId = "link-developers-sdk-kt"
val publishVersion = "0.0.1-SNAPSHOT"
val publishName = "link-developers-sdk-kt"
val publishDescription = "SDK for line-blockchain in Kotlin"
val publishProjectUrl = "https://github.com/ryukato/developers-sdk/blob/master/developers-sdk-kt/README.md"
val publishLicense = "MIT License"
val developerList = listOf(
    mapOf("id" to "ryukato", "name" to "Yoonyoul Yoo", "email" to "ryukato79@gmail.com")
)
val gitHubProjectMainSourceUrl = "https://github.com/ryukato/developers-sdk/tree/master/developers-sdk-kt/src/main/kotlin"

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
                    connection.set("scm:git:git://ryukato/developers-sdk.git")
                    developerConnection.set("scm:git:ssh://ryukato/developers-sdk.git")
                    url.set("https://github.com/ryukato/developers-sdk/tree/master/developers-sdk-kt")
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
            // TODO change URLs to point to maven repository
            val releasesRepoUrl = uri("$buildDir/repos/releases")
            val snapshotsRepoUrl = uri("$buildDir/repos/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenSDK"])
}

bintray {
    user = project.property("BINTRAY_USERNAME").toString()
    key = project.property("BINTRAY_KEY").toString()
    setPublications("mavenSDK")

    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = "com.github.ryukato"
        name = "developers-sdk-kt"
        userOrg = "ryukato79"
        websiteUrl = "https://blog.simon-wirtz.de"
        setLicenses("MIT")
        vcsUrl = "https://github.com/ryukato/developers-sdk.git"
        setLicenses("MIT")
        version(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.VersionConfig> {
            name = publishVersion
            desc = ""
            released = "2020/12/13"
            vcsTag = "v1.0.0"
        })
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


tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("developers-sdk-kt")
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL(gitHubProjectMainSourceUrl))
                remoteLineSuffix.set("#L")
            }
        }
    }
}
