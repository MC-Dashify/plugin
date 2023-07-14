/*
 * Copyright (C) 2023 Dashify
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    /**
     * This object is for pretty printing JSON that handles Ktor requests.
     */
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