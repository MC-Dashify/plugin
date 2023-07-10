package cc.dashify.plugin.manager

/**
 * RuntimeManager
 * manages the runtime
 */
object RuntimeManager {
    /**
     * getMemory()
     * returns the memory usage of the server
     * @return HashMap<String, Any>
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