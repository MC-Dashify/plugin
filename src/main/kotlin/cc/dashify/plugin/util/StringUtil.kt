package cc.dashify.plugin.util

import kotlin.random.Random

/**
 * StringUtil
 *
 * String handling functions
 */
object StringUtil {
    /**
     * Generates a random string in a specified length.
     *
     * @param length [Int][The length of the string.]
     * @return [String] The generated string.
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
