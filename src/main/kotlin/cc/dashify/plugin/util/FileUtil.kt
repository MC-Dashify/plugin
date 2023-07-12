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
    /**
     * Check inner file size of a specific directory.
     *
     * @param dir [File][The file that you want to check.]
     * @return [Long] The size of the file.
     */
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