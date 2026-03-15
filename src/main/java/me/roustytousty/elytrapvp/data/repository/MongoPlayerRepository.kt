package me.roustytousty.elytrapvp.data.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import me.roustytousty.elytrapvp.data.api.MongoManager
import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.model.PlayerData
import org.bson.Document
import java.util.*

class MongoPlayerRepository : PlayerRepository {

    private val collection: MongoCollection<Document> =
        MongoManager.getDatabase("ElytraPVP").getCollection("PlayerData")

    override fun loadPlayer(uuid: UUID): PlayerData? {

        val doc = collection.find(Filters.eq("_id", uuid.toString())).firstOrNull()
            ?: return null

        return documentToPlayerData(doc)
    }

    override fun createPlayer(uuid: UUID, username: String): PlayerData {

        val playerData = PlayerData(
            uuid = uuid,
            username = username
        )

        val doc = playerDataToDocument(playerData)
        collection.insertOne(doc)

        return playerData
    }

    override fun savePlayer(playerData: PlayerData) {

        val doc = playerDataToDocument(playerData)

        collection.replaceOne(
            Filters.eq("_id", playerData.uuid.toString()),
            doc
        )
    }

    override fun getTopLeaderboard(stat: String, limit: Int): List<LeaderboardEntry> {

        return collection.find()
            .sort(Sorts.descending(stat))
            .limit(limit)
            .map { documentToLeaderboardEntry(stat, it) }
            .toList()
    }

    override fun getPlayerRank(uuid: UUID, stat: String): Int {

        val doc = collection.find(Filters.eq("_id", uuid.toString())).firstOrNull()
            ?: return -1

        val value = doc.getInteger(stat) ?: return -1

        return collection.countDocuments(
            Filters.gte(stat, value)
        ).toInt()
    }

    private fun documentToLeaderboardEntry(stat: String, doc: Document): LeaderboardEntry {

        return LeaderboardEntry(
            uuid = UUID.fromString(doc.getString("_id")),
            username = doc.getString("username") ?: "Unknown",
            value = doc.getInteger(stat, 0)
        )
    }

    private fun documentToPlayerData(doc: Document): PlayerData {

        return PlayerData(
            uuid = UUID.fromString(doc.getString("_id")),
            username = doc.getString("username"),

            isBuildMode = doc.getBoolean("isBuildMode", false),

            gold = doc.getInteger("gold", 0),

            kills = doc.getInteger("kills", 0),
            deaths = doc.getInteger("deaths", 0),
            killstreak = doc.getInteger("killstreak", 0),
            topKillstreak = doc.getInteger("topKillstreak", 0),

            helmetLevel = doc.getInteger("helmetLevel", 0),
            elytraLevel = doc.getInteger("elytraLevel", 0),
            leggingsLevel = doc.getInteger("leggingsLevel", 0),
            bootsLevel = doc.getInteger("bootsLevel", 0),
            swordLevel = doc.getInteger("swordLevel", 0),
            shearsLevel = doc.getInteger("shearsLevel", 0)
        )
    }

    private fun playerDataToDocument(playerData: PlayerData): Document {

        return Document()
            .append("_id", playerData.uuid.toString())
            .append("username", playerData.username)

            .append("isBuildMode", playerData.isBuildMode)

            .append("gold", playerData.gold)

            .append("kills", playerData.kills)
            .append("deaths", playerData.deaths)
            .append("killstreak", playerData.killstreak)
            .append("topKillstreak", playerData.topKillstreak)

            .append("helmetLevel", playerData.helmetLevel)
            .append("elytraLevel", playerData.elytraLevel)
            .append("leggingsLevel", playerData.leggingsLevel)
            .append("bootsLevel", playerData.bootsLevel)
            .append("swordLevel", playerData.swordLevel)
            .append("shearsLevel", playerData.shearsLevel)
    }
}