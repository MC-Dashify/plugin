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
        return hashMapOf(
            "maxMemory" to "${Runtime.getRuntime().maxMemory() / (1024 * 1024)} MB",
            "totalMemory" to "${Runtime.getRuntime().totalMemory() / (1024 * 1024)} MB",
            "freeMemory" to "${Runtime.getRuntime().freeMemory() / (1024 * 1024)} MB",
            "usedMemory" to "${
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)
            } MB"
        )
    }
}
