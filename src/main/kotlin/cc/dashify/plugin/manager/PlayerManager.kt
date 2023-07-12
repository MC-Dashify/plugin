package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import cc.dashify.plugin.util.DashifyUtil.validateUUID
import com.google.gson.Gson
import io.ktor.http.*
import net.kyori.adventure.text.Component.text

/**
 * @author aroxu, pybsh
 */

/**
 * PlayerManager
 *
 * Player management functions
 */
object PlayerManager {
    /**
     * Get a list of players.
     *
     * @return [HashMap] Result of the opperation. Type: <String, List<HashMap<String, String>>>
     */
    fun getPlayerList(): HashMap<String, List<HashMap<String, String>>> = hashMapOf("worlds" to plugin.server.onlinePlayers.map { hashMapOf("uuid" to it.uniqueId.toString(), "name" to it.name) })

    /**
     * Retrieve specific player info.
     *
     * @param playerUUIDString [String][Player UUID]
     * @return [HashMap] Result of the operation. Type: <String, Any?>
     */
    fun getPlayerInfo(playerUUIDString: String): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()

        val playerUUID = validateUUID(playerUUIDString, result) ?: return result
        val player = plugin.server.getPlayer(playerUUID)

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
     * Perform a kick or ban to a specific player
     *
     * @param type [String][Check if it's a ban or not.]
     * @param playerUUIDString [String][Player UUID]
     * @param reasonContext [String][Reason for kick/ban in String, it can be null.]
     * @return [HashMap] Result of the operation. Type: <String, Any>
     */
    suspend fun managePlayer(type: String, playerUUIDString: String, reasonContext: String?): HashMap<String, Any?> {
        val result = HashMap<String, Any?>()

        val playerUUID = validateUUID(playerUUIDString, result) ?: return result
        val player = plugin.server.getPlayer(playerUUID)

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
                if (type == "ban") player.banPlayerFull(reason)
                if (reason.isBlank()) player.kick() else player.kick(text(reason))
                result["statusCode"] = HttpStatusCode.OK
            }
        }.onFailure {
            result["statusCode"] = HttpStatusCode.InternalServerError
            result["error"] = it.stackTraceToString()
        }

        return result
    }
}