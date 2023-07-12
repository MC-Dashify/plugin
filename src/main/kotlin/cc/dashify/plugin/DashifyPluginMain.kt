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

import cc.dashify.plugin.DashifyServer.isServerRunning
import cc.dashify.plugin.DashifyServer.startKtor
import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.DashifyUtil
import cc.dashify.plugin.util.DashifyUtil.enabled
import cc.dashify.plugin.util.StringUtil
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.plugin.java.JavaPlugin
import org.mindrot.jbcrypt.BCrypt
import java.util.function.Function


/**
 * @author aroxu, pybsh, lambdynma
 */

/**
 * Dashify Plugin Main
 *
 * The main class of the plugin.
 */
class DashifyPluginMain : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        ConfigHandler.initConfig()

        val manager = PaperCommandManager(this, CommandExecutionCoordinator.simpleCoordinator(), Function.identity(), Function.identity())
        manager.command(DashifyCommand.registerCommand(manager))

        if (config.getString("key")?.trim().isNullOrBlank()) {
            val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
            config.set("key", key)
            DashifyUtil.key = key
            saveConfig()
        }

        startKtor()

        if (enabled) {
            logger.info("Dashify enabled.")
        }
        else {
            logger.warning("Dashify disabled.")
        }
    }

    override fun onDisable() {
        if (isServerRunning) {
            DashifyServer.server.stop(0, 0)
        }
    }
}