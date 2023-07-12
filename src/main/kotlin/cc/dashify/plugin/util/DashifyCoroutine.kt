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
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author aroxu, pybsh
 */

/**
 * DashifyCoroutine
 *
 * Coroutines related functions defined in here.
 */

object DashifyCoroutine {

    /**
     * await for [Runnable].
     *
     * @param task [Unit]
     */
    suspend fun await(task: () -> Unit) {
        suspendCoroutine { continuation: Continuation<Boolean> ->
            plugin.server.scheduler.runTask(plugin, Runnable {
                task()
                continuation.resume(true)
            })
        }
    }
}
