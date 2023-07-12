/*
 * Copyright (C) 2023 Dashify
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.dashify.plugin

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.router.dashify
import com.github.shynixn.mccoroutine.bukkit.launch
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers

/**
 * @author aroxu, pybsh, lambdynma
 */

object DashifyServer {
    var isServerRunning: Boolean = false
    val server = embeddedServer(Netty,
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
     * Starts the Ktor server.
     */
    fun startKtor() {
        plugin.launch(Dispatchers.IO) {
            server.start(wait = true)
        }
        isServerRunning = true
    }
}