/**
 * @author lambdynma
 */

/**
 * This dependency file is a list of libraries and constants that can be used in build.gradle.kts.
 *
 * Structure may be changed by owner's decision.
 */

object Dependency {
    /**
     * Kotlin version object and constants.
     */
    object Kotlin {
        const val Version = "1.9.0"
    }

    /**
     * Paper version & api version constants.
     */
    object Paper {
        const val Version = "1.20.1"
        const val API = "1.19"
    }

    val repos = arrayListOf(
        "https://repo.papermc.io/repository/maven-public/"
    )

    private const val ktorVersion = "2.3.2"

    /**
     * List of libraries.
     */
    object Libraries {
        val Lib = arrayListOf(
            "cloud.commandframework:cloud-paper:1.8.3",
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.12.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.12.1",
            "org.mindrot:jbcrypt:0.4",
            "io.ktor:ktor-server-core-jvm:$ktorVersion",
            "io.ktor:ktor-server-netty-jvm:$ktorVersion",
            "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion",
            "io.ktor:ktor-serialization-jackson:$ktorVersion"
        )

        val LibCore = arrayListOf(
            "cloud.commandframework:cloud-paper:1.8.3",
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.12.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.12.1",
            "org.mindrot:jbcrypt:0.4",
            "io.ktor:ktor-server-core-jvm:$ktorVersion",
            "io.ktor:ktor-server-netty-jvm:$ktorVersion",
            "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion",
            "io.ktor:ktor-serialization-jackson-jvm:$ktorVersion"
        )
    }
}