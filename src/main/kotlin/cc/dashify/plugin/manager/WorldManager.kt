package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import cc.dashify.plugin.util.DashifyUtil.validateUUID
import cc.dashify.plugin.util.FileUtil.getFolderSize
import io.ktor.http.*
import org.bukkit.GameRule

object WorldManager {
    fun getWorldsList(): HashMap<String, List<HashMap<String, out Any>>> = hashMapOf("worlds" to plugin.server.worlds.map { hashMapOf("uuid" to it.uid.toString(), "name" to it.name) })

    /**
     * getWorldInfo()
     * return world info
     * @param reqWorldUUID String
     * @return HashMap<String, Any>
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
                val world = plugin.server.getWorld(worldUUID) ?: return@let

                await {
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
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }
        return result
    }
}