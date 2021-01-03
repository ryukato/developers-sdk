import java.net.URI

fun getProperty(name: String, project: org.gradle.api.Project): String? {
    System.getenv().forEach {
        println( "${it.key} -> ${it.value}")
    }
    val systemProperty = System.getenv(name) ?: System.getProperty(name)
    println("systemProperty: $systemProperty with name: $name")
    return systemProperty ?: project.property(name)?.toString()
}

fun mavenReleaseRepoUrl(): URI = URI.create(releasesRepoUrl)
fun mavenSnapshotsRepoUrl(): URI = URI.create(snapshotsRepoUrl)
fun releaseTargetRepoUrl(version: String): URI {
    return if (version.endsWith("SNAPSHOT")) mavenSnapshotsRepoUrl() else mavenReleaseRepoUrl()
}
