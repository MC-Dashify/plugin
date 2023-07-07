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
            host = if(ConfigHandler["exposePort"].toString().toBoolean()) { "0.0.0.0" } else { "localhost" }
        }
        classLoader = (DashifyPluginMain)::class.java.classLoader
    }
)

/**
 * startKtor()
 * starts the Ktor server
 */
fun startKtor() {
    if (isServerRunning) return
    Thread {
        server.start(wait = true)
    }.start()
    isServerRunning = true
}

/**
 * stopKtor()
 * stops the Ktor server
 */
fun stopKtor() {
    if (!isServerRunning) return
    server.stop(20, 20)
    isServerRunning = false
}

/**
 * restart()
 * restarts the Ktor server
 */
fun restart() {
    stopKtor()
    startKtor()
}

/**
 * checkIsKtorServerRunning()
 * checks if the Ktor server is running
 * @return Boolean
 */
fun checkIsKtorServerRunning(): Boolean {
    return isServerRunning
}

