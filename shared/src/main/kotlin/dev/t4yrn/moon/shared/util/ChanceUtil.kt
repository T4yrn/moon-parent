package dev.t4yrn.moon.shared.util

import kotlin.random.Random

object ChanceUtil {

    @JvmStatic
    fun pick(range: Int): Int {
        return this.pick(0,range)
    }

    @JvmStatic
    fun pick(min: Int, max: Int): Int {
        return Random.nextInt(min, max)
    }

    @JvmStatic
    fun random():Boolean {
        return Random.nextBoolean()
    }

    @JvmStatic
    fun percent(chance: Int): Boolean {
        return Random.nextInt(100) >= 100 - chance
    }

    @JvmStatic
    fun percent(chance: Double): Boolean {
        return Random.nextDouble(100.0) >= 100.0 - chance
    }

}