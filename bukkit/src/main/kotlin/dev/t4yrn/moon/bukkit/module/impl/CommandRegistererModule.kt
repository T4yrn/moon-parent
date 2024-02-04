package dev.t4yrn.moon.bukkit.module.impl

import dev.t4yrn.moon.bukkit.command.CommandService
import dev.t4yrn.moon.bukkit.command.CommandAPI
import dev.t4yrn.moon.bukkit.Moon
import dev.t4yrn.moon.bukkit.command.rank.RankCommand
import dev.t4yrn.moon.bukkit.provider.ColorProvider
import dev.t4yrn.moon.bukkit.provider.RankProvider
import dev.t4yrn.moon.bukkit.module.Module
import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.rank.data.Rank
import org.bukkit.ChatColor


class CommandRegistererModule(name: String, priority: Int) : Module(name, priority) {

    override fun onEnable(instance: Moon) {
        val commandAPI: CommandService = CommandAPI.get(Moon.get())

        commandAPI.bind(Rank::class.java).toProvider(RankProvider(MoonAPI.get()!!.rankHandler))
        commandAPI.bind(ChatColor::class.java).toProvider(ColorProvider())

        listOf(
            //Admin
            RankCommand("rank")
        ).forEach { it ->
            commandAPI.register(it, it.name)
        }

        commandAPI.registerCommands()
    }

    override fun onDisable(instance: Moon) {
        // Implementar si es necesario
    }
}