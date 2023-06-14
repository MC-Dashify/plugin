package io.dashify.plugin

import io.dashify.plugin.util.ConfigHandler
import io.dashify.plugin.util.StringUtil
import org.bukkit.plugin.java.JavaPlugin
import org.mindrot.jbcrypt.BCrypt

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

        startKtor()
        logger.info("dashify-plugin Enabled.")
    }

    override fun onDisable() {
        stopKtor()
    }
}
