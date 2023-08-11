package cc.dashify.plugin.manager

import cc.dashify.plugin.DashifyPluginMain
import cc.dashify.plugin.util.DashifyCoroutine
import cc.dashify.plugin.util.FileUtil
import com.sun.management.OperatingSystemMXBean
import io.ktor.http.*
import io.sentry.Sentry
import org.bukkit.GameRule
import java.lang.management.ManagementFactory
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object DataManager {
    fun getWorldsList(): HashMap<String, Any> {
        val worlds = arrayListOf<HashMap<String, Any>>()
        DashifyPluginMain.plugin.server.worlds.forEach { worlds.add(hashMapOf("uuid" to it.uid, "name" to it.name)) }
        return hashMapOf("worlds" to worlds)
    }

    fun getPlayerList(): HashMap<String, Any> {
        val players = arrayListOf<HashMap<String, Any>>()
        DashifyPluginMain.plugin.server.onlinePlayers.forEach { players.add(hashMapOf("uuid" to it.uniqueId, "name" to it.name)) }
        return hashMapOf("players" to players)
    }

    fun getMemory(): HashMap<String, Any> {
        return hashMapOf(
            "maxMemory" to "${Runtime.getRuntime().maxMemory() / (1024 * 1024)} MB",
            "totalMemory" to "${Runtime.getRuntime().totalMemory() / (1024 * 1024)} MB",
            "freeMemory" to "${Runtime.getRuntime().freeMemory() / (1024 * 1024)} MB",
            "usedMemory" to "${(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)} MB"
        )
    }

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

    suspend fun getWorldInfo(worldUuid: String): Pair<Any, HashMap<String, Any>> {
        val map = HashMap<String, Any>()

        val uuid = runCatching { UUID.fromString(worldUuid) }.getOrNull()
            ?: return Pair(
                HttpStatusCode.BadRequest,
                map.apply {
                    this["error"] = "invalid UUID"
                }
            )

        val world = DashifyPluginMain.plugin.server.getWorld(uuid)
            ?: return Pair(
                HttpStatusCode.BadRequest,
                map.apply {
                    this["error"] = "World not found"
                }
            )

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
            }
            Pair(HttpStatusCode.OK, map)
        }.getOrElse {
            map["error"] = it.stackTraceToString()
            Sentry.captureException(it)
            Pair(HttpStatusCode.InternalServerError, map)
        }
    }

    fun getPlayerInfo(playerUuid: String): Pair<Any, HashMap<String, Any?>> {
        val result = HashMap<String, Any?>()

        val uuid = runCatching { UUID.fromString(playerUuid) }.getOrNull()
            ?: return Pair(
                HttpStatusCode.BadRequest,
                result.apply {
                    this["statusCode"] = HttpStatusCode.BadRequest
                    this["error"] = "invalid UUID"
                }
            )

        val player = DashifyPluginMain.plugin.server.getPlayer(uuid)
            ?: return Pair(HttpStatusCode.BadRequest, result.apply {
                this["statusCode"] = HttpStatusCode.NotFound
                this["error"] = "Player not found"
            })

        result["name"] = player.name
        result["uuid"] = player.uniqueId.toString()
        result["ping"] = player.ping
        result["clientBrandName"] = player.clientBrandName
        result["avatar"] = "https://mc-heads.net/avatar/${player.uniqueId}"
        return Pair(HttpStatusCode.OK, result)
    }
}
