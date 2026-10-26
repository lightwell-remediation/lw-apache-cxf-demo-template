# 👋 Hello World with Spring Boot and Apache CXF

A single JAX-RS endpoint, `GET /services/hello`, protected by Apache CXF OAuth 2.0.

The resource server uses `OAuthRequestFilter` from `cxf-rt-rs-security-oauth2`. There is no external identity provider: the filter accepts only the bearer token configured in `application.properties`.

| Component | Version |
| --- | --- |
| Java | 17 |
| Spring Boot | 4.0.9 (latest 4.0.x patch; Apache CXF 4.2.2 is built with 4.0.6) |
| Apache CXF | 4.2.2 |

## 📋 Prerequisites

- JDK 17 (a full JDK, not a JRE)
- Maven 3.9 or later

Maven uses `JAVA_HOME`, not the `java` command on your `PATH`. Check both:

```bash
java -version
mvn -version
```

`mvn -version` must report Java 17 or newer from a JDK that includes `javac`. On this machine, `java` is Temurin 17, but Maven is using the Red Hat OpenJDK 25 runtime at `/usr/lib/jvm/java-25-openjdk`. That install is a JRE: it has no `javac` and no `lib/ct.sym`, so compilation fails with `release version 17 not supported`.

Before building, configure Maven access to the Lightwell repositories:

```bash
export LW_USERNAME="REPLACE_WITH_YOUR_LW_USERNAME"
export LW_PASSWORD="REPLACE_WITH_YOUR_LW_PASSWORD"
export LW_MAVEN_REPO_URL="REPLACE_WITH_THE_LIGHTWELL_REMEDIATED_REPOSITORY_URL"
```

Always pass the configuration file `settings.xml`:

```bash
mvn -s settings.xml <goal>
```

## 🔨 Build

```bash
mvn -s settings.xml clean package
```

The runnable jar is `target/lw-apache-cxf-sample-1.0.0-SNAPSHOT.jar`.

## 🧪 Test

```bash
mvn -s settings.xml test
```

The tests start the application and check that:

- a request without a bearer token is rejected with HTTP 401
- a request with an unknown bearer token is rejected with HTTP 401
- a request with the sample bearer token returns `Hello World`

## ▶️ Run locally

```bash
mvn -s settings.xml spring-boot:run
```

The application listens on port 8080. The sample access token is `hello-world-token` (`hello.oauth.access-token` in `src/main/resources/application.properties`).

A call without a token is rejected:

```bash
curl -i http://localhost:8080/services/hello
```

A call with the sample bearer token returns the greeting:

```bash
curl -i -H "Authorization: Bearer hello-world-token" http://localhost:8080/services/hello
```

```text
Hello World
```

You can also run the packaged jar:

```bash
java -jar target/lw-apache-cxf-sample-1.0.0-SNAPSHOT.jar
```

## 🧩 Application components

Running the next Maven command you can get a fully detailed list of the dependencies of the project:

```bash
mvn -s settings.xml dependency:tree > target/dependecy-tree.txt
```

To generate the SBOM, run:

```bash
syft scan file:./target/lw-apache-cxf-sample-1.0.0-SNAPSHOT.jar \
    -o cyclonedx-json@1.6 \
    --source-name "lw-apache-cxf-sample" \
    --source-version "1.0.0-SNAPSHOT" \
    --scope deep-squashed \
    --enrich java \
    --select-catalogers "+java-archive-cataloger" \
    | jq '.metadata.component.type = "application"' \
    > target/lw-apache-cxf-sample-1.0.0-SNAPSHOT-sbom.json
```

