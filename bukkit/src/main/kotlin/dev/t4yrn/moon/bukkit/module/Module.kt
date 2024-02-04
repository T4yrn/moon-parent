package dev.t4yrn.moon.bukkit.module

import dev.t4yrn.moon.bukkit.Moon

abstract class Module(val name: String, val priority: Int) {

    var enabled: Boolean = false

    abstract fun onEnable(instance: Moon)

    abstract fun onDisable(instance: Moon)

}