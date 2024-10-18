package me.roustytousty.elytrapvp.api

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
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
    private val uri =
        "mongodb+srv://roustytousty:JIEOjRyzV0XxVotF@roustytoustydb.nkqhd.mongodb.net/?retryWrites=true&w=majority&appName=RoustyToustyDB"

    var mongoClient: MongoClient? = null

    fun setupDbOnEnable() {
        try {
            println(uri)
            mongoClient = MongoClients.create(uri)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupDbOnDisable() {
        try {
            if (mongoClient != null) {
                mongoClient?.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //TODO: NOTIFY FRIENDS
    fun setupPlayerOnJoin(plr: Player) {
        val db = mongoClient?.getDatabase("ElytraPVP")
        val collection = db?.getCollection("PlayerData")!!
        try {
            val playerDoc = collection.find(Filters.eq("_id", "${plr.uniqueId}")).first()
            if (playerDoc != null) {
                println("Player Joined Before")

                val newVariables = mapOf(
                    "gold" to 0,

                    "isStaff" to false,
                    "isBuildMode" to false,

                    "kills" to 0,
                    "killstreak" to 0,
                    "deaths" to 0,

                    "helmetLevel" to 0,
                    "elytraLevel" to 0,
                    "leggingsLevel" to 0,
                    "bootsLevel" to 0,
                    "swordLevel" to 0,
                    "shearsLevel" to 0,
                )

                val updates = mutableListOf<Bson>()
                for ((key, defaultValue) in newVariables) {
                    if (!playerDoc.containsKey(key)) {
                        updates.add(Updates.set(key, defaultValue))
                    }
                }

                if (updates.isNotEmpty()) {
                    collection.updateOne(Filters.eq("_id", "${plr.uniqueId}"), Updates.combine(*updates.toTypedArray()))
                    println("Updated player document with new variables.")
                }

                createCacheData(plr)
            } else {
                println("Player Is New!")
                val doc = Document("_id", "${plr.uniqueId}")
                    .append("username", plr.name)
                    .append("gold", 0)
                    .append("kills", 0)
                    .append("killstreak", 0)
                    .append("deaths", 0)
                    .append("isStaff", false)
                    .append("isBuildMode", false)
                    .append("helmetLevel", 0)
                    .append("elytraLevel", 0)
                    .append("leggingsLevel", 0)
                    .append("bootsLevel", 0)
                    .append("swordLevel", 0)
                    .append("shearsLevel", 0)


                collection.insertOne(doc)
            }
            createCacheData(plr)
            //Functions.setupPlayerComponent(plr)
            /*if (isnew) {
                Bukkit.broadcast(Component.text("Please Welcome, ").append(plr.displayName().append(Component.text("!"))))
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Functions.setupPlayerComponent(plr)

    }

    fun getAccountsCollection(): MongoCollection<Document> {
        val db = mongoClient?.getDatabase("ElytraPVP")
        val collection = db?.getCollection("PlayerData")!!

        return collection
    }

    fun updatePlayerDocument(plr: OfflinePlayer, newdoc: Document) {
        val collection = getAccountsCollection()
        val search = Filters.eq("_id", "${plr.uniqueId}")
        val olddoc = collection.find(search).first()

        if (olddoc != null) {
            try {
                collection.replaceOne(olddoc, newdoc)
                val updateOperation = Updates.combine(newdoc.map { Updates.set(it.key, it.value) })
                collection.updateOne(search, updateOperation)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPlayerDocument(plr: OfflinePlayer): Document? {
        val db = mongoClient?.getDatabase("ElytraPVP")
        val collection = db?.getCollection("PlayerData")!!
        val olddoc = collection.find(Filters.eq("_id", "${plr.uniqueId}")).first()
        if (olddoc != null) {
            return olddoc
        }
        return null
    }

    fun getTopPlayers(stat: String, num: Int): List<Document> {
        val db = mongoClient?.getDatabase("ElytraPVP")
        val collection = db?.getCollection("PlayerData")!!

        return collection.find()
            .sort(Document(stat, -1))
            .limit(num)
            .toList()
    }

    fun getPlayerRank(plr: OfflinePlayer, stat: String): Int {
        val db = mongoClient?.getDatabase("ElytraPVP")
        val collection = db?.getCollection("PlayerData")!!

        val playerDoc = collection.find(Filters.eq("_id", "${plr.uniqueId}")).firstOrNull()
        val playerStat = playerDoc?.getInteger(stat) ?: return -1

        val rank = collection.countDocuments(Filters.gte(stat, playerStat)).toInt()

        return rank
    }

    fun setPlayerDocVal(plr: OfflinePlayer, key: String, newValue: Any?) {
        val doc = getPlayerDocument(plr)

        doc?.set(key, newValue)
        doc?.replace(key, doc.getValue(key), newValue)

        if (doc != null) {
            updatePlayerDocument(plr, doc)
        }
    } // jolo

//    fun setPlayerDocVal(plr: OfflinePlayer, key: String, newValue: Any?) {
//        val collection = getAccountsCollection()
//        val search = Filters.eq("_id", "${plr.uniqueId}")
//
//        // Update the value in MongoDB directly
//        collection.updateOne(search, Updates.set(key, newValue))
//    }


    fun createCacheData(plr: Player) {
        val doc = getPlayerDocument(plr) ?: return
        val plrCache = "cached.${plr.name}"
        val cachedConfig = CacheConfig.getConfig() // Main.instance?.config!!
        //val cached = Main.cacheConfig!!
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

    fun saveAllCache() {
        for (plr: Player in Bukkit.getOnlinePlayers()) {
            saveCachedData(plr)
            Bukkit.getOnlinePlayers().forEach { it.kick(Component.text("Please Rejoin.")) }
        }
    }
}
//    fun getPlayerTitle(plr : Player): String{
//        val user = LuckPermsProvider.get().userManager.loadUser(plr.getUniqueId())
//        val primaryGroup: String = user.get().primaryGroup
//
//        return primaryGroup
//  