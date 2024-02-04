package dev.t4yrn.moon.bukkit.provider

import dev.t4yrn.moon.bukkit.command.argument.CommandArg
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType
import dev.t4yrn.moon.shared.rank.RankHandler
import dev.t4yrn.moon.shared.rank.data.Rank
import org.bukkit.ChatColor
import org.jetbrains.annotations.Nullable
import java.util.stream.Collectors

class RankProvider(private val manager: RankHandler) : ParameterType<Rank>() {

    override fun doesConsumeArgument(): Boolean {
        return true
    }

    override fun isAsync(): Boolean {
        return false
    }

    @Nullable
    @Throws(CommandExitMessage::class)
    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Rank? {
        if (arg.get() == null) {
            return null
        }

        return manager.findByName(arg.get()) ?: throw CommandExitMessage("${ChatColor.RED}There's not existing rank with that name.")
    }

    override fun argumentDescription(): String {
        return "<rank>"
    }

    override fun getSuggestions(prefix: String): List<String> {
        return manager.getRanks()
            .stream()
            .map { it.name }
            .filter { name -> prefix.isEmpty() || name.startsWith(prefix) }
            .collect(Collectors.toList())
    }
}
