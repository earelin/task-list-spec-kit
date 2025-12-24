pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("io.micronaut.platform.catalog") version "4.6.1"
}

rootProject.name = "tasklist-backend"

include("app")
