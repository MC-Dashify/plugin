package io.dashify.plugin.manager

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import io.dashify.plugin.util.DashifyCoroutine.await
import io.ktor.http.*
import org.bukkit.BanList
import org.bukkit.entity.Player
import java.util.*

object PlayerManager {
    fun getPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<String>()
        plugin.server.onlinePlayers.forEach { players.add(it.uniqueId.toString()) }

        return hashMapOf("players" to players)
    }

    fun getPlayerInfo(playerUid: String): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()

        try { UUID.fromString(playerUid) }
        catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "invalid UUID"
            return result
        }

        val player = plugin.server.getPlayer(UUID.fromString(playerUid))
        if (player == null ) {
            result["statusCode"] = HttpStatusCode.NotFound
            result["error"] = "Player not found"
            return result
        }

        var clientBrandName: String? = null
        try { clientBrandName = Player::class.java.getMethod("getClientBrandName").invoke(player).toString() }
        catch (_: Exception) { }

        result["name"] = player.name
        result["uuid"] = player.uniqueId.toString()
        result["ping"] = player.ping
        result["clientBrandName"] = clientBrandName
        result["avatar"] = "https://crafatar.com/avatars/${player.uniqueId}"

        return result
    }

    suspend fun managePlayer(type: String, playerUid: String, reason: String?): HashMap<String, Any> {
        val result = HashMap<String, Any>()

        try { UUID.fromString(playerUid) }
        catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "invalid UUID"
            return result
        }

        val player = plugin.server.getPlayer(UUID.fromString(playerUid))
        if (player == null ) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "Player not found"
            return result
        }

        runCatching {
            await {
                if (type == "ban") { plugin.server.getBanList(BanList.Type.NAME).addBan(player.name, reason, null, null) }
                player.kickPlayer(reason)
                result["statusCode"] = HttpStatusCode.OK
            }
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }

        return result
    }
}