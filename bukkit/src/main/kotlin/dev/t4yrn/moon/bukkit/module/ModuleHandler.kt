package dev.t4yrn.moon.bukkit.module

import dev.t4yrn.moon.bukkit.Moon
import dev.t4yrn.moon.bukkit.module.impl.CommandRegistererModule
import dev.t4yrn.moon.bukkit.module.impl.ListenerRegistryModule
import dev.t4yrn.moon.shared.util.TimeUtil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.logging.Logger

object ModuleHandler {

    private val logger: Logger = Logger.getLogger(ModuleHandler::class.java.name)
    private val modules: ConcurrentMap<Module, Int> = ConcurrentHashMap()

    fun initialLoad() {
        listOf(
            ListenerRegistryModule("Listener Module", 1),
            CommandRegistererModule("Command Register Module", 2)
        ).forEach { module ->
            registerModule(module)
        }

        init(Moon.get())
    }

    private fun init(instance: Moon) {
        val startTime = System.currentTimeMillis()

        val moduleList = sortModules()

        moduleList.forEach { module ->
            val moduleStartTime = System.currentTimeMillis()

            try {
                module.onEnable(instance)
                module.enabled = true
            } catch (e: Exception) {
                logger.severe("[Moon] Failed to initialize module '${module.name}': ${e.message}")
            }

            val moduleEndTime = System.currentTimeMillis()
            val moduleElapsedTime = moduleEndTime - moduleStartTime

            logger.info("[Moon] Module '${module.name}' has been loaded successfully in ${TimeUtil.formatTime(moduleElapsedTime)} seconds.")
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        logger.info("[Moon] All modules have been loaded successfully in ${TimeUtil.formatTime(elapsedTime)} seconds.")
    }

    fun shutdown(instance: Moon) {
        logger.info("[Moon] Shutting down the module system...")

        val startTime = System.currentTimeMillis()

        val moduleList = sortModules()

        moduleList.forEach { module ->
            val moduleStartTime = System.currentTimeMillis()

            try {
                module.onDisable(instance)
                module.enabled = false
            } catch (e: Exception) {
                logger.severe("[Moon] Failed to stop module '${module.name}': ${e.message}")
            }

            val moduleEndTime = System.currentTimeMillis()
            val moduleElapsedTime = moduleEndTime - moduleStartTime

            logger.info("[Moon] Module '${module.name}' has been stopped successfully in ${TimeUtil.formatTime(moduleElapsedTime)} seconds.")
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        logger.info("[Moon] All modules have been stopped successfully in ${TimeUtil.formatTime(elapsedTime)} seconds.")
    }

    fun registerModule(module: Module) {
        if (!modules.containsKey(module)) {
            modules[module] = module.priority
        } else {
            logger.severe("[Moon] Failed to register module '${module.name}'. A module with the same name is already registered.")
        }
    }

    fun enableModule(module: Module, instance: Moon) {
        if (module.enabled) {
            logger.warning("[Moon] Module '${module.name}' is already enabled.")
        }

        val moduleToDisable = findModuleByName(module.name)
        moduleToDisable?.onEnable(instance)
        moduleToDisable?.enabled = true

        logger.info("[Moon] Module '${module.name}' has been disabled successfully.")
    }

    fun disableModule(module: Module, instance: Moon) {
        if (!module.enabled) {
            logger.warning("[Moon] Module '${module.name}' is already disabled.")
        }

        val moduleToEnable = findModuleByName(module.name)
        moduleToEnable?.onDisable(instance)
        moduleToEnable?.enabled = false

        logger.info("[Moon] Module '${module.name}' has been enabled successfully.")
    }

    fun findModuleByName(name: String?): Module? {
        if (name == null) return null
        return modules.keys.stream().filter { module -> name == module.name }.findFirst().orElse(null)
    }

    private fun sortModules(): List<Module> {
        val sortedModules = ArrayList(modules.keys)
        sortedModules.sortBy { it.priority }
        return sortedModules
    }
}