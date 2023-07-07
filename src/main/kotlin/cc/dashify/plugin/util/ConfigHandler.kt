package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

/**
 * @author aroxu
 */

const val CONFIG_VERSION = 1

/**
 * ConfigHandler
 * handle config file
 */
object ConfigHandler {
    private lateinit var config: FileConfiguration

    /**
     * initConfig()
     * init config file
     */
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

    /**
     * get()
     * get config value
     * @param key String
     * @return Any?
     */
    operator fun get(key: String) = config.get(key)

    /**
     * getSection()
     * get config section
     * @param key String
     * @return FileConfiguration?
     */
    fun getSection(key: String) = config.getConfigurationSection(key)

    /**
     * set()
     * set config value
     * @param key String
     * @param value Any
     */
    operator fun set(key: String, value: Any) {
        config.set(key, value)
        plugin.saveConfig()
    }
}
