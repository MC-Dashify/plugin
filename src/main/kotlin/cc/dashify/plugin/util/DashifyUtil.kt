package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import io.ktor.http.*
import java.util.*

object DashifyUtil {
    var key = plugin.config.getString("key") ?: ""

    fun validateUUID(uuid: String, result: HashMap<String, Any?>): UUID? {
        return try {
            UUID.fromString(uuid)
        } catch (e: IllegalArgumentException) {
            result["statusCode"] = HttpStatusCode.BadRequest
            result["error"] = "Invalid UUID"
            null
        }
    }
}