package me.roustytousty.elytrapvp.data.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import me.roustytousty.elytrapvp.data.api.MongoManager
import org.bson.Document

class MongoEventRepository : EventRepository {

    private val collection: MongoCollection<Document> =
        MongoManager.getDatabase("ElytraPVP").getCollection("EventData")

    override fun loadContributions(eventName: String): Int {
        val doc = collection.find(Filters.eq("_id", eventName)).firstOrNull()
        return doc?.getInteger("contributions") ?: 0
    }

    override fun saveContributions(eventName: String, contributions: Int) {
        val doc = Document("_id", eventName)
            .append("contributions", contributions)

        collection.replaceOne(
            Filters.eq("_id", eventName),
            doc,
            ReplaceOptions().upsert(true)
        )
    }
}