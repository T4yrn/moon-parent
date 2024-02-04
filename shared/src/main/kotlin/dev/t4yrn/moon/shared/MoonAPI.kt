package dev.t4yrn.moon.shared

import dev.t4yrn.moon.shared.backend.BackendManager
import dev.t4yrn.moon.shared.backend.option.MongoDBOption
import dev.t4yrn.moon.shared.profile.ProfileHandler
import dev.t4yrn.moon.shared.rank.RankHandler

class MoonAPI(option: MongoDBOption) {

    val backendManager: BackendManager

    val profileHandler: ProfileHandler
    val rankHandler: RankHandler

    init {
        instance = this

        backendManager = BackendManager(option)

        profileHandler = ProfileHandler
        rankHandler= RankHandler
    }

    fun onDisable() {
        profileHandler.shutdown()
        rankHandler.shutdown()

        backendManager.shutdown()

        instance = null
    }

    companion object {
        private var instance: MoonAPI? = null

        fun get(): MoonAPI? {
            return instance
        }
    }

}