@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlinSpringBoot()
    publish()
}

group = projectGroupId
version = "0.0.1"

kotlinDependencies()
ktorDependencies()
junitDependencies()
kotlinCoroutineDependencies()
springBootDependencies()

dependencies {
    implementation(project(":developers-sdk-kt"))
    implementation(Libs.springBootAutoconfigure)
    // kotlin logging
    implementation("io.github.microutils:kotlin-logging:1.7.10")
}

val publishVersion = version as String
val publicationName = "mavenSpringBootSupport"
publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = publishGroupId
            artifactId = SpringBootSupport.publishArtifactId
            version = publishVersion

            from(components["java"])

            pom {
                name.set(SpringBootSupport.publishName)
                description.set(SpringBootSupport.publishDescription)
                url.set(SpringBootSupport.publishProjectUrl)
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

    sign(publishing.publications[publicationName])
}

bintray {
    user = getProperty("BINTRAY_USERNAME", project)
    key = getProperty("BINTRAY_KEY", project)
    setPublications("mavenSDK")

    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = publishGroupId
        name = SpringBootSupport.publishArtifactId
        userOrg = bintrayUserOrg
        setLicenses("MIT")
        vcsUrl = gitRepositoryUrl
    })
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
tasks.bootJar {
    enabled = false
}
tasks.jar {
    enabled = true
}

tasks.withType<com.jfrog.bintray.gradle.tasks.BintrayUploadTask> {
    doFirst {
        publishing.publications
            .filterIsInstance<MavenPublication>()
            .forEach { publication ->
                val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                if (moduleFile.exists()) {
                    publication.artifact(object : org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(moduleFile) {
                        override fun getDefaultExtension() = "module"
                    })
                }
            }
    }
}
