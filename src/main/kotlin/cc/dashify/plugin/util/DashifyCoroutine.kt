package cc.dashify.plugin.util

import cc.dashify.plugin.DashifyPluginMain
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * DashifyCoroutine
 * handle coroutine
 */
object DashifyCoroutine {
    /**
     * await()
     * await for runnable
     * @param runnable Runnable
     */
    suspend fun await(runnable: Runnable) {
        suspendCoroutine { continuation: Continuation<Boolean> ->
            DashifyPluginMain.plugin.server.scheduler.scheduleSyncDelayedTask(DashifyPluginMain.plugin) {
                runnable.run()
                continuation.resume(true)
            }
        }
    }
}
