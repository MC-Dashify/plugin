package io.dashify.plugin

import org.bukkit.plugin.java.JavaPlugin

class DashifyPluginMain : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        saveDefaultConfig()
        logger.info("dashify-plugin Enabled.")
    }
}