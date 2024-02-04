package dev.t4yrn.moon.bukkit

import dev.t4yrn.moon.bukkit.module.ModuleHandler
import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.backend.option.MongoDBOption
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

class Moon : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        api = MoonAPI(getMongoOptions())

        ModuleHandler.initialLoad()

        CompletableFuture.runAsync {
            try {
                Thread.sleep(3000)
                serverLoaded = true
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDisable() {
        serverLoaded = false

        api.onDisable()
    }

    private lateinit var api: MoonAPI
    var serverLoaded = false

    companion object {
        fun get(): Moon {
            return getPlugin(Moon::class.java)
        }
    }

    fun getMongoOptions(): MongoDBOption {
        val host = config.getString("MONGO.HOST")
        val port = config.getInt("MONGO.PORT")
        val database = config.getString("MONGO.DATABASE")
        val username = config.getString("MONGO.AUTHENTICATION.USERNAME")
        val password = config.getString("MONGO.AUTHENTICATION.PASSWORD")
        val authentication = config.getBoolean("MONGO.AUTHENTICATION.ENABLE")

        return MongoDBOption(host, port, database, username, password, authentication)
    }
}
