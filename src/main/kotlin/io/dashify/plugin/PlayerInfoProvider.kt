package io.dashify.plugin

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import org.bukkit.entity.Player
import java.util.*

object PlayerInfoProvider {
    fun getPlayerList(): JsonObject {
        val players = arrayListOf<String>()
        plugin.server.onlinePlayers.forEach { players.add(it.uniqueId.toString()) }

        return JsonObject(mapOf("players" to Json.encodeToJsonElement(players)))
    }
    fun getPlayerInfo(playerUid: String): JsonObject{
        val player = plugin.server.getPlayer(UUID.fromString(playerUid))!!
        var clientBrandName: String? = null

        try {
            clientBrandName = Player::class.java.getMethod("getClientBrandName").invoke(player).toString()
        } catch (_: Exception) {}

        val playerInfo = mapOf(
            "name" to JsonPrimitive(player.name),
            "uuid" to JsonPrimitive(player.uniqueId.toString()),
            "ping" to JsonPrimitive(player.ping),
            "clientBrandName" to JsonPrimitive(clientBrandName)
        )

        return JsonObject(playerInfo)
    }
}