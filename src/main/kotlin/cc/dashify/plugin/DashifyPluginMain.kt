package cc.dashify.plugin

import cc.dashify.plugin.util.ConfigHandler
import cc.dashify.plugin.util.StringUtil
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

        if (ConfigHandler["enabled"].toString() == "") {
            ConfigHandler["enabled"] = true
        }

        startKtor()
        logger.info("dashify-plugin Enabled.")
    }

    override fun onDisable() {
        stopKtor()
    }
}
