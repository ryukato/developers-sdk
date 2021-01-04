@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	maven
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	id("com.jfrog.bintray") version "1.8.5"
	`maven-publish`
	signing
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

val publishVersion = version as String
val springBootSupportArtifactId = "link-developers-sdk-kt-spring-boot-support"
publishing {
	publications {
		create<MavenPublication>("mavenSDK") {
			groupId = publishGroupId
			artifactId = springBootSupportArtifactId
			version = publishVersion

			from(components["java"])

			pom {
				name.set("link-developers-sdk-kt-spring-boot-support")
				description.set(springBootSupportArtifactId)
				url.set("https://github.com/ryukato/developers-sdk/blob/master/developers-sdk-kt-spring-boot-support/README.md")
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
		name = springBootSupportArtifactId
		userOrg = bintrayUserOrg
		setLicenses("MIT")
		vcsUrl = gitRepositoryUrl
	})
}
