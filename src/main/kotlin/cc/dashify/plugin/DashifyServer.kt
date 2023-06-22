package cc.dashify.plugin

import cc.dashify.plugin.router.dashify
import cc.dashify.plugin.util.ConfigHandler
import io.ktor.server.engine.*
import io.ktor.server.netty.*

private var isServerRunning: Boolean = false
private val server = embeddedServer(Netty,
    environment = applicationEngineEnvironment {
        module {
            dashify()
        }
        connector {
            port = ConfigHandler["apiPort"].toString().toInt()
            host = "0.0.0.0"
        }
        classLoader = (DashifyPluginMain)::class.java.classLoader
    }
)

fun startKtor() {
    if (isServerRunning) return
    Thread {
        server.start(wait = true)
    }.start()
    isServerRunning = true
}

fun stopKtor() {
    if (!isServerRunning) return
    server.stop(20, 20)
    isServerRunning = false
}

fun restart() {
    stopKtor()
    startKtor()
}

fun checkIsKtorServerRunning(): Boolean {
    return isServerRunning
}

