package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.DashifyCoroutine.await
import com.google.gson.Gson
import io.ktor.http.*
import io.sentry.Sentry
import net.kyori.adventure.text.Component.text
import java.util.*

/**
 * PlayerManager
 * manage player
 */
object PlayerManager {

    /**
     * managePlayer()
     * kick or ban player
     * @param type String
     * @param playerUid String
     * @param reasonContext String?
     * @return Pair<Any, HashMap<String, Any>>
     */
    suspend fun managePlayer(type: String, playerUid: String, reasonContext: String?): Pair<Any, HashMap<String, Any>> {
        val map = HashMap<String, Any>()

        val uuid = runCatching { UUID.fromString(playerUid) }.getOrNull()
            ?: return Pair(
                HttpStatusCode.BadRequest,
                map.apply {
                    this["error"] = "invalid UUID"
                }
            )

        val player = plugin.server.getPlayer(uuid)
            ?: return Pair(
                HttpStatusCode.BadRequest,
                map.apply {
                    this["error"] = "Player not found"
                }
            )

        val reason = runCatching {
            (Gson().fromJson(reasonContext, HashMap::class.java))["reason"].toString() }.getOrNull()
            ?: return Pair(
                HttpStatusCode.BadRequest,
                map.apply {
                    this["error"] = "Invalid JSON"
                }
            )

        runCatching {
            await {
                if (type == "ban") {
                    player.banPlayer(reason)
                }
                else {
                    if(reason == "") player.kick()
                    else player.kick(text(reason))
                }
                map["statusCode"] = HttpStatusCode.OK
            }
        }.onFailure {
            map["statusCode"] = HttpStatusCode.InternalServerError
            map["error"] = it.stackTraceToString()
            Sentry.captureException(it)
        }

        return Pair(map["statusCode"] ?: HttpStatusCode.OK, map)
    }

}
