package cc.dashify.plugin.util

import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * FileUtil
 * handle file
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
     * getFolderSize()
     * get folder size
     * @param dir File
     * @return String
     */
    fun getFolderSize(dir: File): String {
        val size = dirSize(dir)

        if (size <= 0)
            return "0MB"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }
}
