# Publish open source project to maven(sonatype) repository

## Requirements to publish
To publish your project(mostly packaged in `jar`) to open-source repository such as maven, jcenter, you need provide your signature to prove your publication.
Build system like maven, gradle that you're using, needs to be configured `publishing` and `signing` the publication.

### gpg setup
1. about [GPG](https://librewiki.net/wiki/시리즈:암호의_암도_몰라도_쉽게_하는_GPG)
2. install by `brew install gpg`
  - or you can install GUI tool (https://gpgtools.org/)
3. create key by `gpg --gen-key`
4. check your keys by `gpg --list-keys`
  > Note
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
You need to fill-out the fields surrounded by `"[]"` with your information
```
publishing {
	publications {
		create<MavenPublication>("mavenSDK") {
			groupId = "[your project groupId]"
			artifactId = "[your project artifactId]"
			version = "[version to publish]"

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
1. Create your JIRA account (* we need an account to create a ticket and later)
2. Create a ticket with Sonatype
  * `groupId` is required
    - `groupId` like `com.xxxx` has to refer to your own domain.
    > Note
    >
    > If you don't have your own domain, then you can use `com.github.[your account name]` or `io.github.[your account name]`

  * we need a project page
    - I give them a `README` page for the project page.
  * SCM url is required
    - URL of your project git repository.
  * Some description


## Publishing by github workflow
Usually we need Jenkins or CircleCI to build and publish, but we can build and publish just with github workflow. 
You can see the configured workflow on [Actions](https://github.com/ryukato/developers-sdk/actions) page. 

There are some guide documents as followings.
* [publishing-java-packages-with-gradle](https://docs.github.com/en/free-pro-team@latest/actions/guides/publishing-java-packages-with-gradle)

A workflow consists of event specified by `on` and jobs with steps. You can see the workflow via [gradle-publish.yml](https://github.com/ryukato/developers-sdk/blob/master/.github/workflows/gradle-publish.yml).

### Configure accounts, password and signing key.
The required username, password or api-key for maven and bintray are configured by [Secrets](https://github.com/ryukato/developers-sdk/settings/secrets/actions), and they are passed by system environment variables. So we need to get those  by `System.getenv`.
Not like username, password or api-key, private key for signing is a sort of tricky thing because we need some steps to use it.

#### Add gpg private key to Secrets
We already created the signing key by gpg. With the created signing key, we need to export it and set it to [Secrets](https://github.com/ryukato/developers-sdk/settings/secrets/actions).

1. Export private key by `gpg --export-secret-key -a "[Your user name for the key]" > ~/private.key`
2. Copy the exported key by `cat ~/private.key | pbcopy`
3. Go to [Secrets](https://github.com/ryukato/developers-sdk/settings/secrets/actions)
4. Create a new repository secret with a name - "GPG_SIGNING_KEY".
5. Paste the copied private key.
6. Create a new repository secret with a name - "GPG_SIGNING_PASSWORD"
7. Copy the password of the private key and paste it to value of "GPG_SIGNING_PASSWORD"
8. Add them to environment variables of the build job defined in [gradle-publish.yml](https://github.com/ryukato/developers-sdk/blob/master/.github/workflows/gradle-publish.yml).
9. Configure build scripts to use them. Please refer to `signing` task defined in [build scripts](https://github.com/ryukato/developers-sdk/blob/master/developers-sdk-kt/build.gradle.kts) of `developers-sdk-kt`.
 


