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
