package cc.dashify.plugin

import cc.dashify.plugin.constant.SentryDSN
import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.StringUtil
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import io.sentry.Sentry
import org.bukkit.plugin.java.JavaPlugin
import org.mindrot.jbcrypt.BCrypt
import java.util.function.Function

/**
 * DashifyPluginMain
 * the main class of the plugin.
 */
class DashifyPluginMain : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this

        Sentry.init { options ->
            options.dsn = SentryDSN
            options.tracesSampleRate = 1.0
        }

        ConfigHandler.initConfig()

        if (ConfigHandler["key"].toString().trim() == "") {
            val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
            ConfigHandler["key"] = key
        }

        val commandManager = PaperCommandManager(
            this,
            CommandExecutionCoordinator.simpleCoordinator(),
            Function.identity(),
            Function.identity()
        )
        commandManager.command(DashifyCommand.registerCommand(commandManager))

        startKtor()
        logger.info("dashify-plugin Enabled.")
    }

    override fun onDisable() {
        stopKtor()
    }
}
