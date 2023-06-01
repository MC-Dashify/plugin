package io.dashify.plugin

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

object PlayerInfoProvider {
    fun getPlayerList(): JsonObject {
        val players = arrayListOf<String>()
        DashifyPluginMain.plugin.server.onlinePlayers.forEach { players.add(it.uniqueId.toString()) }

        return JsonObject(mapOf("players" to Json.encodeToJsonElement(players)))
    }
}