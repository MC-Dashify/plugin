package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import java.io.File

/**
 * @author aroxu
 */

/**
 * ConfigHandler
 *
 * Config related functions, constants, variables are defined in here.
 */
object ConfigHandler {
    private const val CONFIG_VERSION = 1

    /**
     * initConfig()
     */

    fun initConfig() {
        plugin.reloadConfig()
        plugin.config.options().copyDefaults(true)
        plugin.saveDefaultConfig()

        if (plugin.config.getInt("configVersion") != CONFIG_VERSION) {
            plugin.logger.warning("Config file is outdated. Overwriting config file...")
            File(plugin.dataFolder, "config.yml").deleteRecursively()
            plugin.saveDefaultConfig()
            plugin.reloadConfig()
        }
    }
}
