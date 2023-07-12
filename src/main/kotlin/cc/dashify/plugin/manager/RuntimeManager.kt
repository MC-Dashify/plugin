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

package cc.dashify.plugin.manager

/**
 * RuntimeManager
 *
 * Runtime management functions
 */
object RuntimeManager {
    /**
     * Retrieves Memory information.
     *
     * @return [HashMap] Result of the operation. Type: <String, Any>
     */
    fun getMemory(): HashMap<String, Any> {
        val runtime = Runtime.getRuntime()

        return hashMapOf(
            "maxMemory" to "${runtime.maxMemory() / (1024 * 1024)} MB",
            "totalMemory" to "${runtime.totalMemory() / (1024 * 1024)} MB",
            "freeMemory" to "${runtime.freeMemory() / (1024 * 1024)} MB",
            "usedMemory" to "${(runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)} MB"
        )
    }
}