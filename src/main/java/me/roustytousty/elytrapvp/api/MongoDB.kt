package me.roustytousty.elytrapvp.api

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import net.kyori.adventure.text.Component
import org.bson.Document
import org.bson.conversions.Bson
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object MongoDB {

    private const val URI = "mongodb+srv://roustytousty:JIEOjRyzV0XxVotF@roustytoustydb.nkqhd.mongodb.net/?retryWrites=true&w=majority&appName=RoustyToustyDB"
    private const val DB_NAME = "ElytraPVP"
    private const val COLLECTION_NAME = "PlayerData"

    var mongoClient: MongoClient? = null

    /*
        MongoDB Default document values to be saved
     */
    private val DEFAULT_VALUES = mapOf(
        "gold" to 0,

        "isStaff" to false,
        "isBuildMode" to false,

        "kills" to 0,
        "killstreak" to 0,
        "topkillstreak" to 0,
        "deaths" to 0,

        "helmetLevel" to 0,
        "elytraLevel" to 0,
        "leggingsLevel" to 0,
        "bootsLevel" to 0,
        "swordLevel" to 0,
        "shearsLevel" to 0
    )



    /*
        Setup MongoDB mongoClient on plugin enable
     */
    fun setupDbOnEnable() {
        try {
            println(URI)
            mongoClient = MongoClients.create(URI)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    /*
        Disable/Close MongoDB mongoClient on plugin disable
     */
    fun setupDbOnDisable() {
        try {
            if (mongoClient != null) {
                mongoClient?.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    /*
        Setup players MongoDB data and start cache creation.
     */
    fun setupPlayerOnJoin(plr: Player) {
        val collection = getAccountsCollection()

        try {
            val playerDoc = collection.find(Filters.eq("_id", "${plr.uniqueId}")).firstOrNull()

            if (playerDoc == null) {
                val newDoc = Document(DEFAULT_VALUES)
                    .append("_id", "${plr.uniqueId}")
                    .append("username", plr.name)

                collection.insertOne(newDoc)

            } else {
                val updates = mutableListOf<Bson>()

                DEFAULT_VALUES.forEach { (key, defaultValue) ->
                    if (!playerDoc.containsKey(key)) {
                        updates.add(Updates.set(key, defaultValue))
                    }
                }

                playerDoc.keys.filterNot { it in DEFAULT_VALUES || it == "_id" || it == "username" }
                    .forEach { key ->
                        updates.add(Updates.unset(key))
                    }

                if (updates.isNotEmpty()) {
                    collection.updateOne(Filters.eq("_id", "${plr.uniqueId}"), Updates.combine(*updates.toTypedArray()))
                }
            }

            createCacheData(plr)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    /*
        Get the PlayerData collection
     */
    private fun getAccountsCollection(): MongoCollection<Document> {
        val db = mongoClient?.getDatabase(DB_NAME)
        val collection = db?.getCollection(COLLECTION_NAME)!!

        return collection
    }



    /*
        Replaces players current document with a new provided document
     */
    private fun updatePlayerDocument(plr: OfflinePlayer, newdoc: Document) {
        val collection = getAccountsCollection()
        val olddoc = getPlayerDocument(plr)

        if (olddoc != null) {
            try {
                collection.replaceOne(olddoc, newdoc)
                val updateOperation = Updates.combine(newdoc.map { Updates.set(it.key, it.value) })
                collection.updateOne(getSearchFilter(plr), updateOperation)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    /*
        Returns players document
     */
    private fun getPlayerDocument(plr: OfflinePlayer): Document? {
        val collection = getAccountsCollection()
        val doc = collection.find(getSearchFilter(plr)).firstOrNull()
        return doc
    }



    /*
        Returns players data search filter
     */
    private fun getSearchFilter(plr: OfflinePlayer): Bson {
        return Filters.eq("_id", "${plr.uniqueId}")
    }



    /*
        Returns top players for a specific stat in descending order
     */
    fun getTopPlayers(stat: String, entries: Int): List<Document> {
        val collection = getAccountsCollection()

        return collection.find()
            .sort(Sorts.descending(stat))
            .limit(entries)
            .toList()
    }



    /*
        Returns a specific players ranking in the leaderboard
     */
    fun getPlayerRank(plr: OfflinePlayer, stat: String): Int {
        val collection = getAccountsCollection()

        val playerDoc = getPlayerDocument(plr)
        val playerStat = playerDoc?.getInteger(stat) ?: return -1

        val rank = collection.countDocuments(Filters.gte(stat, playerStat)).toInt()

        return rank
    }



    /*
        Sets a specific value in the players MongoDB document
     */
    fun setPlayerDocVal(plr: OfflinePlayer, key: String, newValue: Any?) {
        val doc = getPlayerDocument(plr)

        doc?.set(key, newValue)
        doc?.replace(key, doc.getValue(key), newValue)

        if (doc != null) {
            updatePlayerDocument(plr, doc)
        }
    }



    /*
        Creates players cache data and saves it to the CacheConfig
     */
    private fun createCacheData(plr: Player) {
        val doc = getPlayerDocument(plr) ?: return
        val plrCache = "cached.${plr.name}"
        val cachedConfig = CacheConfig.getConfig()

        for (key in doc.keys) {
            var value = doc.get(key)

            if (value == null) {
                value = "missing"
            }
            cachedConfig.set("${plrCache}.${key}", value)
        }

        CacheConfig.save()
        ElytraPVP.instance?.logger?.info("Created cached data for: " + plr.name)
    }



    /*
        Saves players cached data to MongoDB
     */
    fun saveCachedData(plr: Player) {
        val newDoc = Document()
        val cachedConfig = CacheConfig.getConfig()
        val plrDoc = getPlayerDocument(plr)
        val plrCache = "cached.${plr.name}"

        if (plrDoc == null || cachedConfig.get(plrCache) == null) return

        for (key in plrDoc.keys) {
            val configValue = cachedConfig.get("$plrCache.$key")
            if (configValue != null) {
                newDoc.append(key, configValue)
            }
        }

        ElytraPVP.instance?.logger?.info("Saving document: $newDoc")

        updatePlayerDocument(plr, newDoc)

        CacheConfig.set(plrCache, null)
        CacheConfig.save()

        ElytraPVP.instance?.logger?.info("Saved Cache Data for: ${plr.name}")
    }



    /*
        Saves cache data for all players online
     */
    fun saveAllCache() {
        for (plr: Player in Bukkit.getOnlinePlayers()) {
            saveCachedData(plr)
            Bukkit.getOnlinePlayers().forEach { it.kick(Component.text("Please Rejoin.")) }
        }
    }
}