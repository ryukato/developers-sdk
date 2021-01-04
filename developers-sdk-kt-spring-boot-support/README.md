# developers-sdk-kt-spring-boot-support
This project has auto-configuration to support spring-boot applications. The auto-configuration has following beans creation.
* `ApiClient` which provides all the APIs.
* `ApiClientFactory` which is to build `ApiClient` 
* `HttpClientBuilder` `DefaultHttpClientBuilderImpl` is configured as default implementation unless there is no configured a bean type of `HttpClientBuilder`, but if you need your own implementation, then you can implement `HttpClientBuilder` and create the implementation as bean.
* `ApiKeySecretLoader`
* `ApiKeySecretEncoder`
* `ApiKeySecretDecoder`
    - Above three object are important in security, because your api-key and secret should be encoded or encrypted in secure manner. As default instance of `Base64ApiKeySecretSecret` is provided as `ApiKeySecretEncoder` and `ApiKeySecretDecoder`, but you should implement your own `ApiKeySecretEncoder` and `ApiKeySecretDecoder` to keep api-key and secret in secure.

## Sample of ApiClient
You can see how to use `ApiClient` through the test codes in [SpringAutoConfigTest.kt](https://github.com/ryukato/developers-sdk/blob/master/developers-sdk-kt-spring-boot-support/src/test/kotlin/com/yyoo/link/developers/sdk/autoconfigure/SpringAutoConfigTest.kt)

## Download
### Maven
```
<dependency>
  <groupId>com.github.ryukato</groupId>
  <artifactId>link-developers-sdk-kt-spring-boot-support</artifactId>
  <version>0.0.1</version>
</dependency>
```
### Gradle Groovy DSL
```
implementation 'com.github.ryukato:link-developers-sdk-kt-spring-boot-support:0.0.1'
```

### Gradle Kotlin DSL
```
implementation("com.github.ryukato:link-developers-sdk-kt-spring-boot-support:0.0.1")
```
