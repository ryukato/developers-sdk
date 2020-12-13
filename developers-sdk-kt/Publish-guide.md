# Publish open source project to maven(sonatype) repository (WIP)

## Requirements to publish
To publish your project(mostly packaged in `jar`) to open-source repository such as maven, jcenter, you need provide your signature to prove your publication.
And build system like maven, gradle that you're using, needs to be configured `publishing` and `signing` the publication.

### gpg setup
1. about [GPG](https://librewiki.net/wiki/시리즈:암호의_암도_몰라도_쉽게_하는_GPG)
2. install by `brew install gpg`
  - or you can install GUI tool (https://gpgtools.org/)
3. create key by `gpg --gen-key`
4. check your keys by `gpg --list-keys`
  > Check keyId
  >
  > If you can't figure out keyId of each key, then please install [GPG-Suite](https://gpgtools.org/).
  > Using GPG-Suite, you can see list of all keys and check the keyId via its detail.

5. Upload your public key by `gpg2 --keyserver hkp://pool.sks-keyservers.net --send-keys [your keyId]`
  > key servers
  *  hkp://pool.sks-keyservers.net
  *  hkps://keys.openpgp.org

### Configure project
#### Gradle
As you mentioned earlier, we need to configure `publishing` and `signing`, so configuration is consist of those two parts.

##### Configure publishing
You need to fill-out the fields surrouned by `"[]"` with your information
```
publishing {
	publications {
		create<MavenPublication>("mavenSDK") {
			groupId = "com.yyoo"
			artifactId = "link-developers-sdk-kt"
			version = "0.0.1-SNAPSHOT"

			from(components["kotlin"])

			pom {
				name.set("[name of your project]")
				description.set("[description of your project]")
				url.set("[URL of size or page that can describe your project]")

				licenses {
					license {
						name.set("[license of your project]") // "MIT License"
					}
				}
				developers {
					developer {
						id.set("[your github id]")
						name.set("[your name]")
						email.set("[your email]")
					}
				}
				scm {
					connection.set("scm:git:git://[your repository path].git")
					developerConnection.set("scm:git:ssh://[your repository path].git")
					url.set("[your project URL]")
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
			// TODO change URLs to point to your repos, e.g. http://my.org/repo
			val releasesRepoUrl = uri("$buildDir/repos/releases")
			val snapshotsRepoUrl = uri("$buildDir/repos/snapshots")
			url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
		}
	}
}

```

##### Configure signing
1. create `~/.gradle/gradle.properties` file and write configuration like below.

```
NEXUS_USERNAME=[your sonatype account name]
NEXUS_PASSWROD=[your sonatype account password]

sigingin.keyId=[your keyId]
sigingin.password=[your passphrase of the key]
sigingin.secretKeyRingFile=[path of the signature file ] # e.g. ~/.gnupg/trustdb.gpg
```

```
signing {
	sign(publishing.publications["mavenSDK"])
}

```


## To Maven central repository
### Read Guide
* [OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html)
* [Claim Your Namespace on the Central Repository](https://youtu.be/P_3yo-oU1To)
* [Applying for Access to OSSRH](https://youtu.be/0gyF17kWMLg)

### Setup and Publish
#### Setup steps for Sonatype
1. Create your JIRA account (* The account is required to create a ticket and later)
2. Create a ticket with Sonatype
  * `groupId` is required
    - `groupId` like `com.xxxx` has to refer to your own domain.
    > If you don't have your own domain
    >
    > Use `com.github.[your account name]` or `io.github.[your account name]`

  * project page is required
    - I give them a `README` page for the project page.
  * SCM url is required
    - URL of your project git repository.
  * And some description
