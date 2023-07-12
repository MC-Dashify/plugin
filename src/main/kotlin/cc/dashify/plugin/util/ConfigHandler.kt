/*
 * Copyright (C) 2023 Dashify
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
