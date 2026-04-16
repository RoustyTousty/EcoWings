package me.roustytousty.elytrapvp.data.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import me.roustytousty.elytrapvp.data.api.MongoManager
import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.model.PunishmentEntry
import me.roustytousty.elytrapvp.data.model.PunishmentType
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.perk.PerkType
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

    override fun getValueAtOffset(stat: String, offset: Int): Int {
        val doc = collection.find()
            .sort(Sorts.descending(stat))
            .skip(offset - 1)
            .projection(Document(stat, 1))
            .limit(1)
            .first() ?: return 0

        return doc.getInteger(stat, 0)
    }

    private fun documentToLeaderboardEntry(stat: String, doc: Document): LeaderboardEntry {

        return LeaderboardEntry(
            uuid = UUID.fromString(doc.getString("_id")),
            username = doc.getString("username") ?: "Unknown",
            value = doc.getInteger(stat, 0)
        )
    }

    private fun documentToPlayerData(doc: Document): PlayerData {
        val rawPunishments = doc.getList("punishments", Document::class.java) ?: emptyList()
        val punishments = rawPunishments.mapNotNull {
            try {
                PunishmentEntry(
                    type = PunishmentType.valueOf(it.getString("type")),
                    reason = it.getString("reason"),
                    issuer = it.getString("issuer"),
                    timestamp = it.getLong("timestamp"),
                    durationMillis = it.getLong("durationMillis")
                )
            } catch (e: Exception) {
                null
            }
        }.toMutableList()

        return PlayerData(
            uuid = UUID.fromString(doc.getString("_id")),
            username = doc.getString("username"),
            punishments = punishments,

            isBuildMode = doc.getBoolean("isBuildMode", false),

            gold = doc.getInteger("gold", 50),
            shards = doc.getInteger("rebirthTokens", 0),

            rebirths = doc.getInteger("rebirths", 0),
            kills = doc.getInteger("kills", 0),
            deaths = doc.getInteger("deaths", 0),
            killstreak = doc.getInteger("killstreak", 0),
            recordKillstreak = doc.getInteger("recordKillstreak", 0),

            helmetLevel = doc.getInteger("helmetLevel", 0),
            elytraLevel = doc.getInteger("elytraLevel", 0),
            leggingsLevel = doc.getInteger("leggingsLevel", 0),
            bootsLevel = doc.getInteger("bootsLevel", 0),
            swordLevel = doc.getInteger("swordLevel", 0),
            shearsLevel = doc.getInteger("shearsLevel", 0),
            pickaxeLevel = doc.getInteger("pickaxeLevel", 0),
            axeLevel = doc.getInteger("axeLevel", 0),

            unlockedPerks = getValidUnlockedPerks(doc),
            equippedPerks = getValidEquippedPerks(doc),
            unlockedPerkSlots = doc.getInteger("unlockedPerkSlots", 1),

            unlockedTrimPatterns = doc.getList("unlockedTrimPatterns", String::class.java)?.toMutableList() ?: mutableListOf(),
            unlockedTrimMaterials = doc.getList("unlockedTrimMaterials", String::class.java)?.toMutableList() ?: mutableListOf(),
            activeTrimPattern = doc.getString("activeTrimPattern") ?: "",
            activeTrimMaterial = doc.getString("activeTrimMaterial") ?: ""
        )
    }

    private fun playerDataToDocument(playerData: PlayerData): Document {
        val punishmentDocs = playerData.punishments.map {
            Document()
                .append("type", it.type.name)
                .append("reason", it.reason)
                .append("issuer", it.issuer)
                .append("timestamp", it.timestamp)
                .append("durationMillis", it.durationMillis)
        }

        return Document()
            .append("_id", playerData.uuid.toString())
            .append("username", playerData.username)
            .append("punishments", punishmentDocs)

            .append("isBuildMode", playerData.isBuildMode)

            .append("gold", playerData.gold)
            .append("rebirthTokens", playerData.shards)

            .append("rebirths", playerData.rebirths)
            .append("kills", playerData.kills)
            .append("deaths", playerData.deaths)
            .append("killstreak", playerData.killstreak)
            .append("recordKillstreak", playerData.recordKillstreak)

            .append("helmetLevel", playerData.helmetLevel)
            .append("elytraLevel", playerData.elytraLevel)
            .append("leggingsLevel", playerData.leggingsLevel)
            .append("bootsLevel", playerData.bootsLevel)
            .append("swordLevel", playerData.swordLevel)
            .append("shearsLevel", playerData.shearsLevel)
            .append("pickaxeLevel", playerData.pickaxeLevel)
            .append("axeLevel", playerData.axeLevel)

            .append("unlockedPerks", playerData.unlockedPerks)
            .append("equippedPerks", playerData.equippedPerks)
            .append("unlockedPerkSlots", playerData.unlockedPerkSlots)

            .append("unlockedTrimPatterns", playerData.unlockedTrimPatterns)
            .append("unlockedTrimMaterials", playerData.unlockedTrimMaterials)
            .append("activeTrimPattern", playerData.activeTrimPattern)
            .append("activeTrimMaterial", playerData.activeTrimMaterial)
    }

    private fun getValidUnlockedPerks(doc: Document): MutableList<String> {
        val rawList = doc.getList("unlockedPerks", String::class.java) ?: return mutableListOf()
        return rawList.filter { PerkType.fromId(it) != null }.toMutableList()
    }

    private fun getValidEquippedPerks(doc: Document): MutableList<String> {
        val rawList = doc.getList("equippedPerks", String::class.java) ?: mutableListOf()
        return MutableList(3) { i ->
            val id = rawList.getOrNull(i) ?: ""
            if (id.isNotEmpty() && PerkType.fromId(id) == null) "" else id
        }
    }
}