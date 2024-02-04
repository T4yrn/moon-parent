package dev.t4yrn.moon.shared.rank.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.util.MongoUtil
import org.bson.Document
import java.util.concurrent.CompletableFuture

class Rank(
    var name: String,
    var displayName: String = name,
    var color: String = "GRAY",
    var prefix: String = "",
    var weight: Int = 1,
    var staff: Boolean = false,
    var media: Boolean = false,
    var hidden: Boolean = false,
    var discordId: String = "",
    var inherits: MutableList<String> = mutableListOf(),
    var permissions: MutableList<String> = mutableListOf()
) : lRank {

    init {
        inherits = mutableListOf()
        permissions = mutableListOf()

        load()
    }

    override fun save(async: Boolean) {
        val document = Document()

        document["name"] = name.takeIf { true } ?: "Error"
        document["displayName"] = displayName.takeIf { true } ?: "Error"

        document["color"] = color.takeIf { true } ?: "GRAY"
        document["prefix"] = prefix.takeIf { true } ?: ""

        document["weight"] = weight

        document["staff"] = staff
        document["media"] = media
        document["hidden"] = hidden

        document["discordId"] = discordId.takeIf { true } ?: "Not Set"

        document["inherits"] = inherits.takeIf { it.isNotEmpty() } ?: ArrayList<String>()
        document["permissions"] = permissions.takeIf { it.isNotEmpty() } ?: ArrayList<String>()

        if (async) {
            CompletableFuture.runAsync {
                MoonAPI.get()?.backendManager?.ranks?.replaceOne(
                    Filters.eq("name", name),
                    document,
                    MongoUtil.REPLACE_OPTIONS
                )
            }
        } else {
            MoonAPI.get()?.backendManager?.ranks?.replaceOne(
                Filters.eq("name", name),
                document,
                MongoUtil.REPLACE_OPTIONS
            )
        }
    }

    override fun load() {
        val document = MoonAPI.get()?.backendManager?.ranks?.find(Filters.eq("name", name))?.first() ?: return

        name = document.getString("name") ?: "Error"
        displayName = document.getString("displayName") ?: "Error"

        color = document.getString("color") ?: "GRAY"
        prefix = document.getString("prefix") ?: ""

        weight = document.getInteger("weight", 0)

        staff = document.getBoolean("staff", false)
        media = document.getBoolean("media", false)
        hidden = document.getBoolean("hidden", false)

        discordId = document.getString("discordId") ?: "Not Set"

        inherits = document.getList("inherits", String::class.java) ?: ArrayList()
        permissions = document.getList("permissions", String::class.java) ?: ArrayList()
    }

    override fun delete() {
        MoonAPI.get()?.rankHandler?.ranks?.remove(name)
        MoonAPI.get()?.backendManager?.ranks?.deleteOne(Filters.eq("name", name))
    }

    fun hasPermission(permission: String): Boolean {
        return permissions.contains(permission)
    }
}