@file:Suppress("UnstableApiUsage")

import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact


plugins {
    kotlin()
    publish()
}

group = projectGroupId
version = "0.0.5"

sourceSets {
    create("integrationTest") {
        java.srcDir(file("src/test-integration/java"))
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/test-integration/kotlin")
        }

        resources.srcDir(file("src/test-integration/resources"))

        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

kotlinDependencies()
ktorDependencies()
junitDependencies()

dependencies {
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

    // bouncy-castle
    implementation("org.bouncycastle:bcprov-jdk15on:1.64")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.4")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.retrofit2:converter-jaxb:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // kotlin logging
    implementation("io.github.microutils:kotlin-logging:1.7.10")
    // logging
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    testImplementation("org.mockito:mockito-core:2.21.0")
    testImplementation("org.mockito:mockito-junit-jupiter:2.23.0")

}

tasks {
    register<Test>("integrationTest") {
        description = "Runs the integration tests."
        group = "verification"
        testClassesDirs = sourceSets["integrationTest"].output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath
        useJUnitPlatform()
        mustRunAfter(test)
    }
}

val publishVersion = version as String

publishing {
    publications {
        create<MavenPublication>("mavenSDK") {
            groupId = publishGroupId
            artifactId = Sdk.publishArtifactId
            version = publishVersion

            from(components["java"])

            pom {
                name.set(Sdk.publishName)
                description.set(Sdk.publishDescription)
                url.set(Sdk.publishProjectUrl)
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

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

