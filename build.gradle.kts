plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version ("7.1.1")
    kotlin("plugin.serialization") version "1.8.21"
}

group = "cc.dashify.plugin"
val ktorVersion = "2.3.1"
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib-jdk8:1.8.21"))
    compileOnly("org.mindrot:jbcrypt:0.4")
    compileOnly("io.sentry:sentry:6.25.2")
    compileOnly("cloud.commandframework:cloud-paper:1.8.3")

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
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
