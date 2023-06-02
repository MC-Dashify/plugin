package io.dashify.plugin

import org.bukkit.plugin.java.JavaPlugin

class DashifyPluginMain : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this

        ConfigHandler.initConfig()
        getCommand("dashify")?.setExecutor(DashifyCommand())
        getCommand("dashify")?.tabCompleter = DashifyCommandTabComplete()

        startKtor()
        logger.info("dashify-plugin Enabled.")
    }

    override fun onDisable() {
        stopKtor()
    }
}
