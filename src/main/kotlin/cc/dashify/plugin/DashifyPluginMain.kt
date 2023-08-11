package cc.dashify.plugin

import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.StringUtil
import io.sentry.Sentry
import org.bukkit.plugin.java.JavaPlugin
import org.mindrot.jbcrypt.BCrypt

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

        ConfigHandler.initConfig()
        getCommand("dashify")?.setExecutor(DashifyCommand())
        getCommand("dashify")?.tabCompleter = DashifyCommandTabComplete()

        if (ConfigHandler["key"].toString().trim() == "") {
            val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())
            ConfigHandler["key"] = key
        }

        Sentry.init { options ->
            options.dsn = "https://77d0f9b5a715c78118e3cff7a4217dc1@o4505685304934400.ingest.sentry.io/4505685420670976"
            options.tracesSampleRate = 1.0
        }

        startKtor()
        logger.info("dashify-plugin Enabled.")
    }

    override fun onDisable() {
        stopKtor()
    }
}
