package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import cc.dashify.plugin.util.FileUtil.getFolderSize
import io.ktor.http.*
import org.bukkit.GameRule
import java.util.*

object WorldManager {
    fun getWorldsList(): HashMap<String, Any> {
        val worlds = arrayListOf<HashMap<String, Any>>()
        plugin.server.worlds.forEach { worlds.add(hashMapOf("uuid" to it.uid, "name" to it.name)) }
        return hashMapOf("worlds" to worlds)
    }

    /**
     * getWorldInfo()
     * return world info
     * @param worldUuid String
     * @return HashMap<String, Any>
     */
    suspend fun getWorldInfo(worldUuid: String): HashMap<String, Any> {
        val result = HashMap<String, Any>()

        try { UUID.fromString(worldUuid) }
        catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "invalid UUID"
            return result
        }

        val worldUuids = plugin.server.worlds.map { it.uid }.toList()
        if (!worldUuids.contains(UUID.fromString(worldUuid))) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "World not found"
            return result
        }

        runCatching {
            val world = plugin.server.getWorld(UUID.fromString(worldUuid))!!

            var entities: Int
            await {
                entities = world.entities.size
                result["name"] = world.name
                result["loadedChunks"] = world.loadedChunks.size
                result["entities"] = entities
                result["player"] = world.players.size
                result["gamerule"] = world.gameRules.mapNotNull { ruleName ->
                    GameRule.getByName(ruleName)?.let { rule ->
                        Pair(ruleName, world.getGameRuleValue(rule))
                    }
                }.toMap()
                result["difficulty"] = world.difficulty.name
                result["size"] = getFolderSize(world.worldFolder)
                result["uuid"] = world.uid
            }
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }
        return result
    }
}
