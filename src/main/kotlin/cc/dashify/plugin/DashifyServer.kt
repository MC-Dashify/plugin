package cc.dashify.plugin

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.router.dashify
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers

object DashifyServer {
    var isServerRunning: Boolean = false
    private val server = embeddedServer(Netty,
        environment = applicationEngineEnvironment {
            module {
                dashify()
            }
            connector {
                port = plugin.config.getInt("apiPort")
                host = if (plugin.config.getBoolean("exposePorts")) {
                    "0.0.0.0"
                } else {
                    "localhost"
                }
            }
            classLoader = DashifyPluginMain::class.java.classLoader
        }
    )

    /**
     * startKtor()
     * starts the Ktor server
     */
    fun startKtor() {
        plugin.launch(Dispatchers.IO) {
            server.start(wait = true)
        }
        isServerRunning = true
    }

    /**
     * stopKtor()
     * stops the Ktor server
     */
    fun stopKtor() {
        server.stop(20, 20)
        isServerRunning = false
    }
}