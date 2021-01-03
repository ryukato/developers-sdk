import java.net.URI

fun getProperty(name: String, project: org.gradle.api.Project): String? {
    val systemProperty = System.getenv(name) ?: System.getProperty(name)
    return try {
        systemProperty ?: project.property(name)?.toString()
    } catch (e: Exception) {
        null
    }
}

fun mavenReleaseRepoUrl(): URI = URI.create(releasesRepoUrl)
fun mavenSnapshotsRepoUrl(): URI = URI.create(snapshotsRepoUrl)
fun releaseTargetRepoUrl(version: String): URI {
    return if (version.endsWith("SNAPSHOT")) mavenSnapshotsRepoUrl() else mavenReleaseRepoUrl()
}
