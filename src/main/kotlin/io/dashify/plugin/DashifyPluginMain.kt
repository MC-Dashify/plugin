package io.dashify.plugin

import org.bukkit.plugin.java.JavaPlugin

class DashifyPluginMain : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        saveDefaultConfig()
        logger.info("dashify-plugin Enabled.")
    }
}
