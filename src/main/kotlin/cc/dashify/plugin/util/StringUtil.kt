package cc.dashify.plugin.util

import kotlin.random.Random

/**
 * StringUtil
 * @author aroxu
 */
object StringUtil {
    /**
     * generateRandomString()
     * generate random string
     * @param length Int
     * @return String
     */
    fun generateRandomString(length: Int): String {
        val charPool = mutableListOf<Char>()
        charPool.addAll(
            ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf(
                '!',
                '@',
                '#',
                '$',
                '%',
                '^',
                '&',
                '*',
                '(',
                ')',
                '/',
                '`',
                '~',
                '-',
                '_',
                '+',
                '='
            )
        )

        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
