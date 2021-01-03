import java.net.URI

fun getProperty(name: String, project: org.gradle.api.Project): String? {
    val projectProperty = project.property(name)
    val systemProperty = System.getProperty(name) ?: System.getenv(name)
    return systemProperty ?: projectProperty?.toString()
}

fun mavenReleaseRepoUrl(): URI = URI.create(releasesRepoUrl)
fun mavenSnapshotsRepoUrl(): URI = URI.create(snapshotsRepoUrl)
fun releaseTargetRepoUrl(version: String): URI {
    return if (version.endsWith("SNAPSHOT")) mavenSnapshotsRepoUrl() else mavenReleaseRepoUrl()
}
