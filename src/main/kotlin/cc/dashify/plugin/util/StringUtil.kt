package cc.dashify.plugin.util

import kotlin.random.Random

object StringUtil {
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