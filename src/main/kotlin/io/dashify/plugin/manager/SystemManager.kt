package io.dashify.plugin.manager

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths


object SystemManager {
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
}