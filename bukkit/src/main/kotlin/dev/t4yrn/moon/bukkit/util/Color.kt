package dev.t4yrn.moon.bukkit.util

import org.bukkit.ChatColor
import org.bukkit.DyeColor

import java.util.stream.Collectors

object Color {
    val LINE: String = "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}--------------------------"

    fun color(value: String): String {
        return ChatColor.translateAlternateColorCodes('&', value)
    }

    fun color(list: List<String>): List<String> {
        return list.stream().map { color(it) }.collect(Collectors.toList())
    }

    fun convertToChatColor(colorName: String): ChatColor {
        return when (colorName.toLowerCase()) {
            "black" -> ChatColor.BLACK
            "dark blue" -> ChatColor.DARK_BLUE
            "dark green" -> ChatColor.DARK_GREEN
            "dark aqua" -> ChatColor.DARK_AQUA
            "dark red" -> ChatColor.DARK_RED
            "dark purple" -> ChatColor.DARK_PURPLE
            "gold" -> ChatColor.GOLD
            "dark gray" -> ChatColor.DARK_GRAY
            "blue" -> ChatColor.BLUE
            "green" -> ChatColor.GREEN
            "aqua" -> ChatColor.AQUA
            "red" -> ChatColor.RED
            "light purple" -> ChatColor.LIGHT_PURPLE
            "yellow" -> ChatColor.YELLOW
            "white" -> ChatColor.WHITE
            else -> ChatColor.GRAY
        }
    }

    fun convert(color: String): String {
        return try {
            val color1 = ChatColor.valueOf(color.toUpperCase())
            when (color1) {
                ChatColor.BLUE -> "Blue"
                ChatColor.DARK_GRAY -> "Dark Gray"
                ChatColor.GRAY -> "Gray"
                ChatColor.GOLD -> "Gold"
                ChatColor.DARK_PURPLE -> "Dark Purple"
                ChatColor.DARK_RED -> "Dark Red"
                ChatColor.DARK_AQUA -> "Dark Aqua"
                ChatColor.DARK_GREEN -> "Dark Green"
                ChatColor.DARK_BLUE -> "Dark Blue"
                ChatColor.LIGHT_PURPLE -> "Pink"
                ChatColor.YELLOW -> "Yellow"
                ChatColor.RED -> "Red"
                ChatColor.AQUA -> "Aqua"
                ChatColor.WHITE -> "White"
                ChatColor.GREEN -> "Green"
                else -> "Gray"
            }
        } catch (e: IllegalArgumentException) {
            "Invalid color"
        }
    }

    fun getWoolData(colorValue: String): Byte {
        val color = ChatColor.valueOf(colorValue)
        val dyeColor = getDyeColor(color)
        return dyeColor.woolData
    }

    private fun getDyeColor(color: ChatColor): DyeColor {
        return when (color) {
            ChatColor.DARK_BLUE, ChatColor.BLUE -> DyeColor.BLUE
            ChatColor.DARK_GREEN -> DyeColor.GREEN
            ChatColor.DARK_AQUA -> DyeColor.CYAN
            ChatColor.AQUA -> DyeColor.LIGHT_BLUE
            ChatColor.DARK_RED, ChatColor.RED -> DyeColor.RED
            ChatColor.DARK_PURPLE -> DyeColor.PURPLE
            ChatColor.GOLD -> DyeColor.ORANGE
            ChatColor.GRAY -> DyeColor.SILVER
            ChatColor.DARK_GRAY -> DyeColor.GRAY
            ChatColor.GREEN -> DyeColor.LIME
            ChatColor.LIGHT_PURPLE -> DyeColor.PINK
            ChatColor.YELLOW -> DyeColor.YELLOW
            ChatColor.WHITE -> DyeColor.WHITE
            else -> DyeColor.BLACK
        }
    }
}