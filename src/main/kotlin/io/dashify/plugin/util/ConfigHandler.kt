package io.dashify.plugin.util

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

/**
 * @author aroxu
 */

const val CONFIG_VERSION = 1

object ConfigHandler {
    private lateinit var config: FileConfiguration
    fun initConfig() {
        plugin.reloadConfig()
        if (!File(plugin.dataFolder, "config.yml").exists()) {
            plugin.logger.warning("Config file not found. Creating config file...")
            plugin.saveDefaultConfig()
        }

        config = plugin.config
        config.options().copyDefaults(true)
        plugin.saveConfig()
        if (config.getInt("configVersion") != CONFIG_VERSION) {
            plugin.logger.warning("Config file is outdated. Overwriting config file...")
            plugin.saveDefaultConfig()
        }
    }

    operator fun get(key: String) = config.get(key)

    fun getSection(key: String) = config.getConfigurationSection(key)

    operator fun set(key: String, value: Any) {
        config.set(key, value)
        plugin.saveConfig()
    }
}