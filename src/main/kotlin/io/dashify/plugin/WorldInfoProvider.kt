package io.dashify.plugin

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import kotlinx.serialization.json.*
import org.bukkit.GameRule
import java.util.*

object WorldInfoProvider {
    fun getWorldsList(): JsonObject {
        val worlds = arrayListOf<String>()
        plugin.server.worlds.forEach { worlds.add(it.uid.toString()) }

        return JsonObject(mapOf("worlds" to Json.encodeToJsonElement(worlds)))
    }

    fun getWorldInfo(worldUid: String): JsonObject{
        val world = plugin.server.getWorld(UUID.fromString(worldUid))

        val gamerule = mutableMapOf<String, JsonElement>()
        world!!.gameRules.forEach { g -> gamerule[g] = JsonPrimitive(world.getGameRuleValue(GameRule.getByName(g)!!).toString()) }

        val worldInfo = mapOf(
            "name" to JsonPrimitive(world.name),
            "loadedChunks" to JsonPrimitive(world.loadedChunks.size),
            // "entities" to JsonPrimitive(world.entities.size), // TODO: java.lang.IllegalStateException: Asynchronous Chunk getEntities call!
            "player" to JsonPrimitive(world.players.size),
            "gamerule" to Json.encodeToJsonElement(gamerule),
            "difficulty" to JsonPrimitive(world.difficulty.name),
            "size" to JsonPrimitive(world.worldFolder.length())
        )

        return JsonObject(worldInfo)
   }

}