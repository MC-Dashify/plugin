package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import io.ktor.http.*
import java.util.*

/**
 * @author lambdynma
 */

/**
 * DashifyUtil
 *
 * This is where all the utility functions are defined.
 */
object DashifyUtil {
    val enabled
        get() = plugin.config.getBoolean("enabled")
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

    fun validateKey(bearerKey: String?): Boolean = key == bearerKey?.removePrefix("Bearer")?.trim()

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