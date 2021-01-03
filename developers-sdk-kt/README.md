# Line developers-sdk
This is the library to help developers to develop an application interacts LINE Blockchain through `LINE Blockchain Developers`, which is providing APIs for a wallet, a dApp(service) and sending transactions to LINE Blockchain system.

You can find the detail of APIs `LINE Blockchain Developers` at [API-Reference](https://docs-blockchain.line.biz/api-guide/API-Reference)
But with this library, you can send requests to `LINE Blockchain Developers` more easily, and don't need to care about generating signature of a request.

This library is being written in Kotlin and has auto-configuration for `Spring-boot`.
So you can use `coroutine` feature and the beans such as `ApiClient`.

## Key objects
### ApiClient
This has functions to call an API of `LINE Blockchain Developers`.

### ApiClientFactory
This creates `ApiClient` instance with given parameters like `apiBaseUrl`, `serviceApiKey`, `serviceApiSecret` and so on.

> Caution
>
> `serviceApiSecret` has to be encoded in secure way, which mean do not use plain `serviceApiSecret`.
> This library provides encoder and decoder, they encode/decode the secret in base64, which is not safe enough.
> So to secure the secret, encode the secret first and decode with your own implementation. if you use `spring-boot`, then you can create the decoder as a bean.  

### ApiKeySecretLoader
This is to load `api-key` and `api-secret`. As mentioned above, Do not use `api-secret` in plain text.
With `ApiKeySecretDecoder` and this, load the encoded or encrypted secret and use it after decoding.

## Get started   
### Get started with Gradle
```
// developers-sdk
implementation("com.yyoo.link.developers.sdk:developers-sdk-kt:${latest-version}"

// ktor
implementation("io.ktor:ktor-client-core:1.3.0")
implementation("io.ktor:ktor-client-cio:1.3.0")
implementation("io.ktor:ktor-client-jackson:1.3.0")
implementation("io.ktor:ktor-client-logging-jvm:1.3.0")
```
### Get started with Maven (not yet possible, config publishing in WIP)
```xml
<properties>
  <developers-sdk.version>latest-version</developers-sdk.version>
  <ktor.version>1.3.0</ktor.version>
</properties>

<dependency>
    <groupId>com.github.ryukato.link.developers.sdk</groupId>
    <artifactId>developers-sdk-kt</artifactId>
    <version>${developers-sdk-kt.latest-version}</version>
</dependency>
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-core</artifactId>
    <version>${ktor.version}</version>
</dependency>
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-cio</artifactId>
    <version>${ktor.version}</version>
</dependency>
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-jackson</artifactId>
    <version>${ktor.version}</version>
</dependency>
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-logging-jvm</artifactId>
    <version>${ktor.version}</version>
</dependency>
```
### With spring-boot (// TODO move to develoeprs-sdk-kt-spring-boot-support)
If you use `spring-boot`, then you can simple use created `ApiClient` instance by auto-configuration.
You can define a sort of service class with `ApiClient` instance injected by spring-framework.
For example,  
```kotlin
@Service
class DevelopersApiService(apiClient: ApiClient) {
    //do something
}
```
