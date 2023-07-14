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

import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * @author aroxu, pybsh
 */

/**
 * FileUtil
 *
 * File handling functions
 */
object FileUtil {
    private fun dirSize(dir: File): Long {
        if (dir.exists()) {
            var result: Long = 0
            val fileList = dir.listFiles()
            for (i in fileList!!.indices) {
                result += if (fileList[i].isDirectory) {
                    dirSize(fileList[i])
                } else {
                    fileList[i].length()
                }
            }
            return result
        }
        return 0
    }

    /**
     * Checks specific directory size.
     *
     * @param dir [File][The directory that you want to check.]
     * @return [String] of file size in B/Kb/MB/GB/TB.
     */
    fun getFolderSize(dir: File): String {
        val size = dirSize(dir)

        if (size <= 0) return "0MB"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }
}