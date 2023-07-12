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
