import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version("7.1.1")
    kotlin("plugin.serialization") version "1.8.21"
}

group = "io.dashify.plugin"
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
    compileOnly("org.mindrot:jbcrypt:0.4")

    compileOnly("io.ktor:ktor-server-core:2.3.0")
    compileOnly("io.ktor:ktor-server-jetty:2.3.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    create<Jar>("sourcesJar") {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }

    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set("dashify-plugin-all")
    }

    register<Jar>("buildJar") {
        archiveBaseName.set("dashify-plugin")
        from(sourceSets["main"].output)
        val plugins = File(rootDir, ".server/plugins/")

        doLast {
            copy {
                from(archiveFile)
                if (File(plugins, archiveFileName.get()).exists()) {
                    File(plugins, archiveFileName.get()).delete()
                }
                into(plugins)
            }
        }
    }
}