package cc.dashify.plugin.manager

import com.google.gson.Gson
import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import io.ktor.http.*
import net.kyori.adventure.text.Component.text
import org.bukkit.BanList
import java.util.*

/**
 * PlayerManager
 * manage player
 */
object PlayerManager {
    /**
     * getPlayerList()
     * return player list
     * @return HashMap<String, Any>
     */
    fun getPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<HashMap<String, Any>>()
        plugin.server.onlinePlayers.forEach { players.add(hashMapOf("uuid" to it.uniqueId, "name" to it.name)) }
        return hashMapOf("players" to players)
    }

    /**
     * getPlayerInfo()
     * return player info
     * @param playerUuid String
     * @return HashMap<String, Any?>
     */
    fun getPlayerInfo(playerUuid: String): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()

        try { UUID.fromString(playerUuid) }
        catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "invalid UUID"
            return result
        }

        val player = plugin.server.getPlayer(UUID.fromString(playerUuid))
        if (player == null) {
            result["statusCode"] = HttpStatusCode.NotFound
            result["error"] = "Player not found"
            return result
        }

        result["name"] = player.name
        result["uuid"] = player.uniqueId.toString()
        result["ping"] = player.ping
        result["clientBrandName"] = player.clientBrandName
        result["avatar"] = "https://mc-heads.net/avatar/${player.uniqueId}"

        return result
    }

    /**
     * managePlayer()
     * kick or ban player
     * @param type String
     * @param playerUid String
     * @param reasonContext String?
     * @return HashMap<String, Any>
     */
    suspend fun managePlayer(type: String, playerUid: String, reasonContext: String?): HashMap<String, Any> {
        val result = HashMap<String, Any>()

        try { UUID.fromString(playerUid) }
        catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "invalid UUID"
            return result
        }

        val player = plugin.server.getPlayer(UUID.fromString(playerUid))
        if (player == null) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "Player not found"
            return result
        }

        val reason: String?
        try {
            reason = (Gson().fromJson(reasonContext, HashMap::class.java) as HashMap<*, *>)["reason"].toString()
        } catch (e: Exception) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "Invalid JSON"
            return result
        }

        runCatching {
            await {
                if (type == "ban") {
                    plugin.server.getBanList(BanList.Type.NAME).addBan(player.name, reason, null, null)
                }
                if (reason == "") {
                    player.kick()
                } else {
                    player.kick(text(reason))
                }
                result["statusCode"] = HttpStatusCode.OK
            }
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }

        return result
    }

}
