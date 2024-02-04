package dev.t4yrn.moon.shared.profile.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.t4yrn.moon.shared.MoonAPI
import dev.t4yrn.moon.shared.util.MongoUtil
import org.bson.Document
import java.util.*
import java.util.concurrent.CompletableFuture

data class Profile(
    var id: UUID,
    var name: String? = null,
    var address: String? = null,
    var email: String? = null,
    var coins: Int = 0,
    //var topRank: Rank? = null,
    var currentServer: String? = null,
    var lastServer: String? = null,
    var discordId: String? = null,
    var firstJoined: Long = 0,
    var lastJoined: Long = 0,
    //var grants: List<Grant>? = null,
    var addresses: MutableList<String> = mutableListOf(),
    var permissions: MutableList<String> = mutableListOf()
) : lProfile {

    init {
        addresses = mutableListOf()
        permissions = mutableListOf()

        load()
    }

    override fun save(async: Boolean) {
        val document = Document()

        document["_id"] = this.id.toString()
        document["name"] = this.name ?: "Error"
        document["address"] = this.address ?: "None"
        document["email"] = this.email ?: "None"
        document["coins"] = this.coins
        document["currentServer"] = this.currentServer ?: "None"
        document["lastServer"] = this.lastServer ?: "None"
        document["discordId"] = this.discordId ?: "Not Set"
        document["firstJoined"] = this.firstJoined
        document["lastJoined"] = this.lastJoined
        //document["grants"] = Grant.serializeGrants(this.grants)
        document["addresses"] = this.addresses
        document["permissions"] = this.permissions

        if (async) {
            CompletableFuture.runAsync {
                MoonAPI.get()?.backendManager?.profiles?.replaceOne(
                    Filters.eq("_id", this.id.toString()),
                    document,
                    MongoUtil.REPLACE_OPTIONS
                )
            }
        } else {
            MoonAPI.get()?.backendManager?.profiles?.replaceOne(
                Filters.eq("_id", this.id.toString()),
                document,
                MongoUtil.REPLACE_OPTIONS
            )
        }
    }

    override fun load() {
        val document = MoonAPI.get()?.backendManager?.profiles?.find(Filters.eq("_id", this.id.toString()))?.first() ?: return

        this.id = UUID.fromString(document.getString("_id"))
        this.name = document.getString("name") ?: "Error"
        this.address = document.getString("address") ?: "None"
        this.email = document.getString("email") ?: "None"
        this.coins = document.getInteger("coins", 0)
        this.currentServer = document.getString("currentServer") ?: "None"
        this.lastServer = document.getString("lastServer") ?: "None"
        this.discordId = document.getString("discordId") ?: "Not Set"
        this.firstJoined = document.getLong("firstJoined") ?: 0L
        this.lastJoined = document.getLong("lastJoined") ?: 0L

        //this.grants = Grant.deserializeGrants(document.getString("grants"))
        //this.grants = this.grants?.filterNotNull()

        val addresses = document.getList("addresses", String::class.java)
        this.addresses.addAll(addresses.orEmpty())

        val perms = document.getList("permissions", String::class.java)
        this.permissions.addAll(perms.orEmpty())
    }

    override fun delete() {
        MoonAPI.get()?.profileHandler?.profiles?.remove(this.id)
        MoonAPI.get()?.backendManager?.profiles?.deleteOne(Filters.eq("_id", this.id.toString()))
    }

    fun hasPermission(value: String): Boolean {
        return this.permissions.contains(value)
    }
}