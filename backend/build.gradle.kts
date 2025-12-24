plugins {
    id("java")
    id("checkstyle")
    id("org.sonarqube") version "7.2.2.6593"
}

allprojects {
    group = "net.earelin.tasklist"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    checkstyle {
        toolVersion = "10.21.4"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        maxWarnings = 0
        maxErrors = 0
    }

    tasks.withType<Checkstyle> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

sonar {
    properties {
        property("sonar.projectKey", "tasklist-backend")
        property("sonar.projectName", "Task List Backend")
        property("sonar.java.checkstyle.reportPaths", "app/build/reports/checkstyle/main.xml,app/build/reports/checkstyle/test.xml")
    }
}
