package dev.t4yrn.moon.bukkit.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        event.joinMessage = null
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        event.quitMessage = null
    }
}