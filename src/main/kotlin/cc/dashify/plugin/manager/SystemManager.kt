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

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths

/**
 * SystemManager
 *
 * System management functions
 */
object SystemManager {
    /**
     * Retrieves system information.
     *
     * @return [HashMap] Result of the operation. Type: <String, Any>
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
}
