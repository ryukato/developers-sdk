rootProject.name = "developers-sdk-kt"
include("developers-sdk-kt")
project(":developers-sdk-kt").projectDir = File("$rootDir/developers-sdk-kt")
include("developers-sdk-kt-spring-boot-support")
project(":developers-sdk-kt-spring-boot-support").projectDir =
    File("$rootDir/developers-sdk-kt-spring-boot-support")
