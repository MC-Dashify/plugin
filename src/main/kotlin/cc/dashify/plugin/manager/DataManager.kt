package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain
import cc.dashify.plugin.util.DashifyCoroutine
import cc.dashify.plugin.util.FileUtil
import com.sun.management.OperatingSystemMXBean
import io.ktor.http.*
import io.sentry.Sentry
import org.bukkit.GameRule
import java.lang.management.ManagementFactory
import java.lang.reflect.Modifier
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


/**
 * DataManager
 * manages data
 */
object DataManager {
    private val getterFieldPrefixes = listOf("get", "has", "is")
    private val primitiveClasses = listOf(
        java.lang.Boolean::class.java,
        java.lang.Byte::class.java,
        java.lang.Short::class.java,
        java.lang.Integer::class.java,
        java.lang.Long::class.java,
        java.lang.Float::class.java,
        java.lang.Double::class.java,
        java.lang.String::class.java
    )

    private fun getFieldValues(obj: Any, stack: Int = 0): Map<String, Any> {
        val map = HashMap<String, Any>()
        for (method in obj.javaClass.declaredMethods) try {
            if (method.canAccess(obj) && method.parameterCount == 0 && getterFieldPrefixes.any {
                    method.name.startsWith(
                        it
                    )
                } && Modifier.isPublic(method.modifiers)) {
                val methodName = method.name.removePrefix("get").replaceFirstChar {
                    if (method.name.startsWith("get") && method.name.getOrNull(4)
                            ?.isUpperCase() != true
                    ) it.lowercaseChar() else it
                }
                val methodValue = method.invoke(obj)
                val javaClass = methodValue?.javaClass ?: continue
                if (javaClass == obj.javaClass || methodName == "hashCode") continue
                map[methodName] = when {
                    javaClass.isEnum -> methodValue.toString()
                    javaClass.isPrimitive || javaClass in primitiveClasses -> methodValue
                    methodValue is Collection<*> -> methodValue.mapNotNull { value ->
                        value?.let { notNull ->
                            getFieldValues(
                                notNull
                            )
                        }
                    }

                    methodValue is Map<*, *> -> methodValue.mapValues { (_, value) ->
                        value?.let { notNull ->
                            getFieldValues(
                                notNull
                            )
                        }
                    }

                    else -> if (stack > 0) continue else getFieldValues(methodValue, stack + 1)
                }
            }

        } catch (exception: Exception) {
//            println(exception)
        }
        return map
    }

    /**
     * getServerInfo()
     * returns world list
     * @return HashMap<String, Any>
     */
    fun getWorldsList(): HashMap<String, Any> {
        val worlds = arrayListOf<HashMap<String, Any>>()
        DashifyPluginMain.plugin.server.worlds.forEach { worlds.add(hashMapOf("uuid" to it.uid, "name" to it.name)) }
        return hashMapOf("worlds" to worlds)
    }

    /**
     * getPlayerList()
     * returns player list
     * @return HashMap<String, Any>
     */
    fun getPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<HashMap<String, Any>>()
        DashifyPluginMain.plugin.server.onlinePlayers.forEach {
            players.add(
                hashMapOf(
                    "uuid" to it.uniqueId, "name" to it.name
                )
            )
        }
        return hashMapOf("players" to players)
    }

    /**
     * getBannedPlayerList()
     * returns banned player list
     * @return HashMap<String, Any>
     */
    fun getBannedPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<HashMap<String, Any?>>()
        DashifyPluginMain.plugin.server.bannedPlayers.forEach {
            players.add(
                hashMapOf(
                    "uuid" to it.uniqueId, "name" to it.name
                )
            )
        }
        return hashMapOf("players" to players)
    }

    /**
     * getBannedIPList()
     * returns system memory info
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

    /**
     * getSysInfo()
     * returns system info
     * @return HashMap<String, Any>
     */
    fun getSysInfo(): HashMap<String, Any> {
        val systemInfo = HashMap<String, Any>()
        val osBean: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val fs: FileStore = Files.getFileStore(Paths.get(System.getProperty("user.dir")).toRealPath().root)

        val cpuLoad: Double = osBean.cpuLoad * 100
        val cpuCores: Int = osBean.availableProcessors
        systemInfo["cpu"] = hashMapOf("cpuLoad" to cpuLoad, "cpuCores" to cpuCores)

        val totalMemory: Long = osBean.totalMemorySize / (1024 * 1024)
        val usedMemory: Long = totalMemory - osBean.freeMemorySize / (1024 * 1024)
        systemInfo["mem"] = hashMapOf("totalMem" to "$totalMemory MB", "usedMem" to "$usedMemory MB")

        val freeSpace = fs.usableSpace / (1024 * 1024)
        val totalSpace = fs.totalSpace / (1024 * 1024)
        systemInfo["disk"] = hashMapOf("freeSpace" to "$freeSpace MB", "totalSpace" to "$totalSpace MB")
        return systemInfo
    }

    /**
     * getWorldInfo()
     * returns world info
     * @param worldUuid String
     * @return Pair<Any, HashMap<String, Any>>
     */
    suspend fun getWorldInfo(worldUuid: String): Pair<Any, HashMap<String, Any>> {
        val map = HashMap<String, Any>()

        val uuid =
            runCatching { UUID.fromString(worldUuid) }.getOrNull() ?: return Pair(HttpStatusCode.BadRequest, map.apply {
                this["error"] = "invalid UUID"
            })

        val world = DashifyPluginMain.plugin.server.getWorld(uuid) ?: return Pair(HttpStatusCode.BadRequest, map.apply {
            this["error"] = "World not found"
        })

        return runCatching {
            DashifyCoroutine.await {
                map["name"] = world.name
                map["loadedChunks"] = world.loadedChunks.size
                map["entities"] = world.entities.size
                map["player"] = world.players.size
                map["gamerule"] = world.gameRules.mapNotNull { ruleName ->
                    GameRule.getByName(ruleName)?.let { rule ->
                        Pair(ruleName, world.getGameRuleValue(rule))
                    }
                }.toMap()
                map["difficulty"] = world.difficulty.name
                map["size"] = FileUtil.getFolderSize(world.worldFolder)
                map["uuid"] = world.uid
                map["raw"] = getFieldValues(world)
            }

            Pair(HttpStatusCode.OK, map)
        }.getOrElse {
            map["error"] = it.stackTraceToString()
            Sentry.captureException(it)
            Pair(HttpStatusCode.InternalServerError, map)
        }
    }

    /**
     * getPlayerInfo()
     * returns player info
     * @param playerUuid String
     * @return Pair<Any, HashMap<String, Any?>>
     */
    fun getPlayerInfo(playerUuid: String): Pair<Any, HashMap<String, Any?>> {
        val result = HashMap<String, Any?>()

        val uuid = runCatching { UUID.fromString(playerUuid) }.getOrNull() ?: return Pair(
            HttpStatusCode.BadRequest,
            result.apply {
                this["statusCode"] = HttpStatusCode.BadRequest
                this["error"] = "invalid UUID"
            })

        val player =
            DashifyPluginMain.plugin.server.getPlayer(uuid) ?: return Pair(HttpStatusCode.BadRequest, result.apply {
                this["statusCode"] = HttpStatusCode.NotFound
                this["error"] = "Player not found"
            })

        result["name"] = player.name
        result["uuid"] = player.uniqueId.toString()
        result["ping"] = player.ping
        result["clientBrandName"] = player.clientBrandName
        result["avatar"] = "https://mc-heads.net/avatar/${player.uniqueId}"
        result["raw"] = getFieldValues(player)

        return Pair(HttpStatusCode.OK, result)
    }
}
