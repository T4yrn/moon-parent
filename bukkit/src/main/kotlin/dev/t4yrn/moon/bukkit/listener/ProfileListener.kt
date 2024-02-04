package dev.t4yrn.moon.bukkit.listener

import dev.t4yrn.moon.bukkit.Moon
import dev.t4yrn.moon.shared.profile.ProfileHandler
import dev.t4yrn.moon.shared.profile.data.Profile
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.concurrent.CompletableFuture

object ProfileListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        if (event.name == null) {
            return
        }

        if (!Moon.get().serverLoaded) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}The server is still being set up!")
            return
        }

        if (event.name.length > 16 || event.name.length < 3) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}You can't join the server with that name length.")
            return
        }

        var profile = ProfileHandler.findById(event.uniqueId)

        if (profile == null) {
            profile = Profile(event.uniqueId, event.name)

            profile.addresses.add(event.address.hostAddress)
            profile.address = event.address.hostAddress

            profile.firstJoined = System.currentTimeMillis()
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}Failed to load your profile. Try again later.")
        }

        profile.name = event.name

        if (!profile.addresses.contains(event.address.hostAddress)) {
            profile.addresses.add(event.address.hostAddress)
        }

        profile.address = event.address.hostAddress
        profile.save(true)

        val toAdd = profile
        CompletableFuture.runAsync { ProfileHandler.profiles.putIfAbsent(toAdd.id, toAdd) }

        event.allow()
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerLoginEvent(event: PlayerLoginEvent) {
        if (!Moon.get().serverLoaded) {
            event.result = PlayerLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}The server is still being set up!"
            return
        }

        if (event.player.name.length > 16 || event.player.name.length < 3) {
            event.result = PlayerLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}You can't join the server with that name length."
            return
        }

        val profile = ProfileHandler.findById(event.player.uniqueId)

        profile?.let {
            it.name = event.player.name
            it.currentServer = Moon.get().config.getString("SERVER-NAME")
            it.lastJoined = System.currentTimeMillis()
            it.save(true)
        }

        event.allow()
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        val profile = ProfileHandler.findById(event.player.uniqueId)
        profile?.let {
            it.lastServer = Moon.get().config.getString("SERVER-NAME")
            it.currentServer = "None"
            it.save(true)
        }
    }
}