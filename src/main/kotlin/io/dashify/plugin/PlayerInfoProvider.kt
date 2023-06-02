package io.dashify.plugin

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import org.bukkit.entity.Player
import java.util.*

object PlayerInfoProvider {
    fun getPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<String>()
        plugin.server.onlinePlayers.forEach { players.add(it.uniqueId.toString()) }

        return hashMapOf("players" to players)
    }
    fun getPlayerInfo(playerUid: String): HashMap<String, Any?> {
        val player = plugin.server.getPlayer(UUID.fromString(playerUid))!!
        var clientBrandName: String? = null

        try {
            clientBrandName = Player::class.java.getMethod("getClientBrandName").invoke(player).toString()
        } catch (_: Exception) {
        }

        return hashMapOf(
            "name" to player.name,
            "uuid" to player.uniqueId,
            "ping" to player.ping,
            "clientBrandName" to clientBrandName,
            "avatar" to "https://crafatar.com/avatars/${player.uniqueId}"
        )
    }
}