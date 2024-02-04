package dev.t4yrn.moon.shared.profile

import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.profile.data.Profile
import dev.t4yrn.moon.shared.util.TimeUtil
import org.bson.Document
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.logging.Logger

object ProfileHandler {

    private val logger: Logger = Logger.getLogger(ProfileHandler::class.java.name)
    val profiles: ConcurrentMap<UUID, Profile> = ConcurrentHashMap()

    init {
        val startTime = System.currentTimeMillis()

        MoonAPI.get()?.backendManager?.profiles?.find()?.forEach { document ->
            val profile = parse(document)
            profiles[profile.id] = profile
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        logger.info("[Moon] Successfully loaded '${profiles.size}' profile${if (profiles.size != 1) "s" else ""} from the database in ${TimeUtil.formatTime(elapsedTime)} seconds.")
    }

    fun shutdown() {
        profiles.values.forEach { profile ->
            profile.save(true)
            profiles.remove(profile.id, profile)
        }
    }

    fun findById(uuid: UUID?): Profile? {
        return uuid?.let { profiles[it] }
    }

    fun findByName(name: String?): Profile? {
        return name?.let { profile ->
            profiles.values.firstOrNull { it.name == profile }
        }
    }

    fun getProfiles(): Collection<Profile> {
        return ProfileHandler.profiles.values
    }

    private fun parse(document: Document): Profile {
        return Profile(UUID.fromString(document.getString("_id")))
    }
}