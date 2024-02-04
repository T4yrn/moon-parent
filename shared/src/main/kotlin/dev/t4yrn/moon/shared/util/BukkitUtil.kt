package dev.t4yrn.moon.shared.util

object BukkitUtil {

    private const val COLOR_CHAR = '\u00A7'

    fun translateColorCodes(text: String): String {
        return this.translateColorCodes('&',text)
    }

    fun translateColorCodes(char: Char,text: String): String {
        val b = text.toCharArray()

        for (i in 0 until b.size - 1) {

            if (b[i] == char && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR
                b[i + 1] = Character.toLowerCase(b[i + 1])
            }

        }

        return String(b)
    }

}