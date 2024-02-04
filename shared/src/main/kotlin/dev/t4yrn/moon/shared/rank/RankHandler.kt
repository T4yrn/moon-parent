package dev.t4yrn.moon.shared.rank

import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.rank.data.Rank
import dev.t4yrn.moon.shared.util.TimeUtil
import org.bson.Document
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.logging.Logger

object RankHandler {

    private val logger: Logger = Logger.getLogger(RankHandler::class.java.name)
    val ranks: ConcurrentMap<String, Rank> = ConcurrentHashMap()

    init {
        val startTime = System.currentTimeMillis()

        if (!ranks.containsKey("Default")) {
            val defaultRank = Rank("Default")

            defaultRank.save(true)
            ranks["Default"] = defaultRank
        }

        MoonAPI.get()?.backendManager?.ranks?.find()?.forEach { document ->
            val rank = parse(document)
            ranks[rank.name] = rank
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        logger.info("[Moon] Successfully loaded '${ranks.size}' rank${if (ranks.size != 1) "s" else ""} from the database in ${TimeUtil.formatTime(elapsedTime)} seconds.")
    }

    fun shutdown() {
        ranks.values.forEach { rank ->
            rank.save(true)
            ranks.remove(rank.name, rank)
        }
    }

    fun findByName(name: String?): Rank? {
        return name?.let { rankName ->
            ranks.values.firstOrNull { it.name == rankName }
        }
    }

    fun getRanks(): Collection<Rank> {
        return ranks.values
    }

    private fun parse(document: Document): Rank {
        return Rank(document.getString("name"))
    }
}