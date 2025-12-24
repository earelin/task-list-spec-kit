plugins {
    id("io.micronaut.application") version "4.6.1"
}

version = "1.0.0"
group = "net.earelin.tasklist"

repositories {
    mavenCentral()
}

dependencies {
    // Micronaut core
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")

    implementation("io.micronaut.servlet:micronaut-http-server-jetty")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    // Configuration
    runtimeOnly("org.yaml:snakeyaml")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:9.0")

    // Testing
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Architecture tests
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
}

application {
    mainClass.set("net.earelin.tasklist.Application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

micronaut {
    runtime("jetty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("net.earelin.tasklist.*")
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion.set("21")
}
