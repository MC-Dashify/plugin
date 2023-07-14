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

package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import cc.dashify.plugin.util.DashifyUtil.validateUUID
import cc.dashify.plugin.util.FileUtil.getFolderSize
import io.ktor.http.*
import org.bukkit.GameRule

/**
 * @author aroxu, pybsh
 */

/**
 * WorldManager
 *
 * World management functions
 */
object WorldManager {
    /**
     * Get a list of worlds.
     *
     * @return [HashMap] Result of the opperation. Type: <String, List<HashMap<String, String>>>
     */
    fun getWorldList(): HashMap<String, List<HashMap<String, String>>> = hashMapOf("worlds" to plugin.server.worlds.map { hashMapOf("uuid" to it.uid.toString(), "name" to it.name) })

    /**
     * Retrieves specific world info.
     *
     * @param reqWorldUUID String
     * @return [HashMap]<String, Any>
     */
    suspend fun getWorldInfo(reqWorldUUID: String): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()
        val worldUUID = validateUUID(reqWorldUUID, result)

        val worldUUIDList = plugin.server.worlds.map { it.uid }.toList()

        if (!worldUUIDList.contains(worldUUID)) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "World not found"
            return result
        }

        runCatching {
            worldUUID?.let {
                val world = plugin.server.getWorld(worldUUID)

                await {
                    if (world != null) {
                        result["name"] = world.name
                        result["loadedChunks"] = world.loadedChunks.size
                        result["entities"] = world.entities.size
                        result["player"] = world.players.size
                        result["gamerule"] = world.gameRules.mapNotNull { ruleName ->
                            GameRule.getByName(ruleName)?.let { rule ->
                                Pair(ruleName, world.getGameRuleValue(rule))
                            }
                        }.toMap()
                        result["difficulty"] = world.difficulty.name
                        result["size"] = getFolderSize(world.worldFolder)
                    }
                }
            }
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }
        return result
    }
}