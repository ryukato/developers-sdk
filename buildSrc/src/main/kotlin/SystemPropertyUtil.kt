import java.net.URI

fun getProperty(name: String, project: org.gradle.api.Project): String? {
    val projectProperty = project.property(name)
    return projectProperty?.toString() ?: System.getProperty(name)?.takeIf { it.isNotBlank() }
}

fun mavenReleaseRepoUrl(): URI = URI.create(releasesRepoUrl)
fun mavenSnapshotsRepoUrl(): URI = URI.create(snapshotsRepoUrl)
fun releaseTargetRepoUrl(version: String): URI {
    return if (version.endsWith("SNAPSHOT")) mavenSnapshotsRepoUrl() else mavenReleaseRepoUrl()
}
