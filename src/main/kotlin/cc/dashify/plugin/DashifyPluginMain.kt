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