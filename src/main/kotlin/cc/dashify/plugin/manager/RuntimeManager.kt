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