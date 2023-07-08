plugins {
    idea
    kotlin("jvm") version Dependency.Kotlin.Version
    kotlin("plugin.serialization") version Dependency.Kotlin.Version
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cc.dashify.plugin"
version = "0.0.1"

repositories {
    mavenCentral()
    Dependency.repos.forEach { maven(it) }
}

dependencies {
    library(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:${Dependency.Paper.Version}-R0.1-SNAPSHOT")
    Dependency.Libraries.Lib.forEach { compileOnly(it) }
    Dependency.Libraries.LibCore.forEach { paperLibrary(it) }
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

    jar {
        archiveBaseName.set("dashify-plugin")
        archiveClassifier.set("")
    }
    runServer {
        minecraftVersion(Dependency.Paper.Version)
        jvmArgs = listOf("-Dcom.mojang.eula.agree=true")
    }
}

idea {
    module {
        excludeDirs.addAll(listOf(file("run"), file(".idea")))
    }
}

paper {
    main = "${project.group}.DashifyPluginMain"
    loader = "${project.group}.DashifyPluginLoader"
    authors = listOf("aroxu", "pybsh")

    generateLibrariesJson = true
    foliaSupported = false

    apiVersion = Dependency.Paper.API
}