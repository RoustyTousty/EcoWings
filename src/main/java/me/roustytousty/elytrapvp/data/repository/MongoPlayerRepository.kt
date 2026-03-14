package me.roustytousty.elytrapvp.data.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import me.roustytousty.elytrapvp.data.api.MongoManager
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
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

        val player = PlayerData(
            uuid = uuid,
            username = username
        )

        val doc = playerDataToDocument(player)
        collection.insertOne(doc)

        return player
    }

    override fun savePlayer(player: PlayerData) {

        val doc = playerDataToDocument(player)

        collection.replaceOne(
            Filters.eq("_id", player.uuid.toString()),
            doc
        )
    }

    override fun getTopPlayers(stat: String, limit: Int): List<PlayerData> {

        return collection.find()
            .sort(Sorts.descending(stat))
            .limit(limit)
            .map { documentToPlayerData(it) }
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

    private fun documentToPlayerData(doc: Document): PlayerData {

        return PlayerData(
            uuid = UUID.fromString(doc.getString("_id")),
            username = doc.getString("username"),

            gold = doc.getInteger("gold", 0),

            kills = doc.getInteger("kills", 0),
            deaths = doc.getInteger("deaths", 0),
            killstreak = doc.getInteger("killstreak", 0),
            topKillstreak = doc.getInteger("topKillstreak", 0),

            helmetLevel = doc.getInteger("helmetLevel", 0),
            elytraLevel = doc.getInteger("elytraLevel", 0),
            leggingsLevel = doc.getInteger("leggingsLevel", 0),
            bootsLevel = doc.getInteger("bootsLevel", 0)
        )
    }

    private fun playerDataToDocument(player: PlayerData): Document {

        return Document()
            .append("_id", player.uuid.toString())
            .append("username", player.username)

            .append("gold", player.gold)

            .append("kills", player.kills)
            .append("deaths", player.deaths)
            .append("killstreak", player.killstreak)
            .append("topKillstreak", player.topKillstreak)

            .append("helmetLevel", player.helmetLevel)
            .append("elytraLevel", player.elytraLevel)
            .append("leggingsLevel", player.leggingsLevel)
            .append("bootsLevel", player.bootsLevel)
    }
}