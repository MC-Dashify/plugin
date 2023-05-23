package io.dashify.plugin

import org.bukkit.plugin.java.JavaPlugin

class DashifyPluginMain : JavaPlugin() {
    override fun onEnable() {
        logger.info("dashify-plugin Enabled.")
    }
}