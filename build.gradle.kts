plugins {
    kotlin("jvm") version "1.8.21"
}

group = "io.dashify.Dashify"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib-jdk8:1.8.21"))
}

kotlin {
    jvmToolchain(17)
}