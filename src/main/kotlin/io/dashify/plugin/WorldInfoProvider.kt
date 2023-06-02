package io.dashify.plugin

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import io.dashify.plugin.util.DashifyCoroutine.await
import io.dashify.plugin.util.FileUtil.getFolderSize
import org.bukkit.GameRule
import java.util.*

object WorldInfoProvider {
    fun getWorldsList(): HashMap<String, Any> {
        val worlds = arrayListOf<String>()
        plugin.server.worlds.forEach { worlds.add(it.uid.toString()) }

        return hashMapOf("worlds" to worlds)
    }

    suspend fun getWorldInfo(worldUid: String): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        runCatching {
            val world = plugin.server.getWorld(UUID.fromString(worldUid))!!

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
            }
        }.onFailure {
            result["error"] = it.stackTraceToString()
        }
        return result
    }
}