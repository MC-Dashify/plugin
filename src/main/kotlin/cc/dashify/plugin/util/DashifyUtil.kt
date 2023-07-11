package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
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

    suspend fun validateKey(bearerKey: String, call: ApplicationCall): Boolean {
        val condition = key == bearerKey.removePrefix("Bearer").trim()
        if (!condition) call.respond(HttpStatusCode.Unauthorized, hashMapOf("error" to "Invalid key."))
        return condition
    }

    object CustomPrettyPrinter : DefaultPrettyPrinter() {
        init {
            super._arrayIndenter = DefaultIndenter()
            super._objectFieldValueSeparatorWithSpaces = _separators.objectFieldValueSeparator.toString() + " "
        }

        override fun createInstance(): CustomPrettyPrinter {
            return CustomPrettyPrinter
        }
    }
}