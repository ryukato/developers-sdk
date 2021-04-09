@file:Suppress("UnstableApiUsage")

import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact


plugins {
    kotlin()
    publish()
}

group = projectGroupId
version = "0.0.4"

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

    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")

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

bintray {
    user = getProperty("BINTRAY_USERNAME", project)
    key = getProperty("BINTRAY_KEY", project)
    setPublications("mavenSDK")

    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = publishGroupId
        name = Sdk.publishArtifactId
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
