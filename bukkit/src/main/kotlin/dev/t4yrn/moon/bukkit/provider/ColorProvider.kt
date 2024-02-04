package dev.t4yrn.moon.bukkit.provider

import dev.t4yrn.moon.bukkit.command.argument.CommandArg
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType
import org.bukkit.ChatColor
import org.jetbrains.annotations.Nullable

class ColorProvider : ParameterType<ChatColor>() {

    override fun doesConsumeArgument(): Boolean {
        return true
    }

    override fun isAsync(): Boolean {
        return false
    }

    @Nullable
    @Throws(CommandExitMessage::class)
    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): ChatColor? {
        if (arg.get() == null) {
            return null
        }

        return try {
            ChatColor.valueOf(arg.get().toUpperCase())
        } catch (e: IllegalArgumentException) {
            throw CommandExitMessage("${ChatColor.RED}There's not existing color with that name.")
        }
    }

    override fun argumentDescription(): String {
        return "<color>"
    }

    override fun getSuggestions(prefix: String): List<String> {
        val suggestion = mutableListOf<String>()

        ChatColor.values().filter { !it.isFormat }.forEach { color ->
            suggestion.add(color.name)
        }

        return suggestion
    }

}