package me.roustytousty.elytrapvp.data.api

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

object MongoManager {

    private lateinit var client: MongoClient

    fun connect(uri: String) {
        client = MongoClients.create(uri)
    }

    fun getDatabase(name: String): MongoDatabase {
        return client.getDatabase(name)
    }

    fun close() {
        client.close()
    }
}