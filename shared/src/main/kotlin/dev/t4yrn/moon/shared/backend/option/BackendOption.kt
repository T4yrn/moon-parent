package dev.t4yrn.moon.shared.backend.option

data class MongoDBOption(

    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val authenticationRequired: Boolean

)