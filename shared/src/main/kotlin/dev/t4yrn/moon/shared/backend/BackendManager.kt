package dev.t4yrn.moon.shared.backend

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import dev.t4yrn.moon.shared.backend.option.MongoDBOption
import org.bson.Document
import java.util.logging.Logger

class BackendManager(option: MongoDBOption) {

    private val logger: Logger = Logger.getLogger(BackendManager::class.java.name)

    private val client: MongoClient
    val profiles: MongoCollection<Document>
    val ranks: MongoCollection<Document>

    init {
        try {
            if (option.authenticationRequired) {
                val credential = MongoCredential.createCredential(option.username, option.database, option.password.toCharArray())

                client = MongoClient(ServerAddress(option.host, option.port), credential, MongoClientOptions.builder().build())
            } else {
                client = MongoClient(option.host, option.port)
            }

            val database: MongoDatabase = client.getDatabase(option.database)

            val collectionStartTime = System.currentTimeMillis()

            loadOrCreate(database, "profiles")
            loadOrCreate(database, "ranks")
            //loadOrCreate(database, "punishments")
            //loadOrCreate(database, "servers")

            val collectionsNames = listOf("profiles", "ranks") //"punishments", "servers"

            val collectionEndTime = System.currentTimeMillis()
            val collectionElapsedTime = collectionEndTime - collectionStartTime

            for (collectionName in collectionsNames) {
                val collectionLoadTime: MutableMap<String, Long> = HashMap()
                collectionLoadTime[collectionName] = collectionElapsedTime
            }

            profiles = database.getCollection("profiles")
            ranks = database.getCollection("ranks")
            //punishments = database.getCollection("punishments")
            //servers = database.getCollection("servers")
        } catch (e: Exception) {
            println()
            logger.severe("Error occurred while connecting to MongoDB. Check the connection parameters and credentials.")
            throw e
        }
    }

    fun shutdown() {
        client.close()
    }

    private fun loadOrCreate(database: MongoDatabase, name: String) {
        if (!database.listCollectionNames().into(ArrayList<String>()).contains(name)) {
            database.createCollection(name)
        }
    }
}