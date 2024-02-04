package dev.t4yrn.moon.bukkit.module.impl

import dev.t4yrn.moon.bukkit.Moon
import dev.t4yrn.moon.bukkit.listener.PlayerListener
import dev.t4yrn.moon.bukkit.listener.ProfileListener
import dev.t4yrn.moon.bukkit.module.Module
import org.bukkit.event.HandlerList

class ListenerRegistryModule(name: String, priority: Int) : Module(name, priority) {

    override fun onEnable(instance: Moon) {
        listOf(
            ProfileListener,
            PlayerListener,
        ).forEach { listener ->
            instance.server.pluginManager.registerEvents(listener, instance)
        }
    }

    override fun onDisable(instance: Moon) {
        listOf(
            ProfileListener
        ).forEach(HandlerList::unregisterAll)
    }
}