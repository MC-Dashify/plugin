object Dependency {
    object Kotlin {
        const val Version = "1.9.0"
    }

    object Paper {
        const val Version = "1.20.1"
        const val API = "1.19"
    }

    val repos = arrayListOf(
        "https://repo.papermc.io/repository/maven-public/"
    )

    private const val ktorVersion = "2.3.2"

    object Libraries {
        private const val monun = "io.github.monun"

        val Lib = arrayListOf(
            "${monun}:tap-api:4.9.6",
            "${monun}:kommand-api:3.1.5",
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.12.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.12.1",
            "org.mindrot:jbcrypt:0.4",
            "io.ktor:ktor-server-core-jvm:$ktorVersion",
            "io.ktor:ktor-server-cors-jvm:$ktorVersion",
            "io.ktor:ktor-server-netty-jvm:$ktorVersion",
            "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion",
            "io.ktor:ktor-serialization-jackson-jvm:$ktorVersion"
        )

        val LibCore = arrayListOf(
            "${monun}:tap-core:4.9.6",
            "${monun}:kommand-core:3.1.5",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.12.1",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.12.1",
            "org.mindrot:jbcrypt:0.4",
            "io.ktor:ktor-server-core-jvm:$ktorVersion",
            "io.ktor:ktor-server-cors-jvm:$ktorVersion",
            "io.ktor:ktor-server-netty-jvm:$ktorVersion",
            "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion",
            "io.ktor:ktor-serialization-jackson-jvm:$ktorVersion"
        )
    }
}