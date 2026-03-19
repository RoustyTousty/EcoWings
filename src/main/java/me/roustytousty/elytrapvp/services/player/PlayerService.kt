package me.roustytousty.elytrapvp.services.player

import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import org.bukkit.entity.Player

class PlayerService(
    private val repository: PlayerRepository,
    private val cache: PlayerCache
) {

    private val statAccessors: Map<String, (PlayerData) -> Any> = mapOf(
        "uuid" to { it.uuid },
        "username" to { it.username },

        "isBuildMode" to { it.isBuildMode },

        "gold" to { it.gold },

        "kills" to { it.kills },
        "deaths" to { it.deaths },
        "killstreak" to { it.killstreak },
        "topKillstreak" to { it.topKillstreak },

        "helmetLevel" to { it.helmetLevel },
        "elytraLevel" to { it.elytraLevel },
        "leggingsLevel" to { it.leggingsLevel },
        "bootsLevel" to { it.bootsLevel },
        "swordLevel" to { it.swordLevel },
        "shearsLevel" to { it.shearsLevel },
        "pickaxeLevel" to { it.pickaxeLevel },
        "axeLevel" to { it.axeLevel }
    )


    fun getOrCreatePlayerData(player: Player): PlayerData {
        val uuid = player.uniqueId

        cache.get(uuid)?.let { return it }

        val fromDb = repository.loadPlayer(uuid)
        if (fromDb != null) {
            cache.put(fromDb)
            return fromDb
        }

        val created = repository.createPlayer(uuid, player.name)
        cache.put(created)
        return created
    }


    fun saveAndUnloadPlayerData(player: Player) {
        val uuid = player.uniqueId

        val data = cache.get(uuid) ?: return

        data.username = player.name

        repository.savePlayer(data)

        cache.remove(uuid)
    }

    fun getDynamicPlayerData(player: Player, stat: String): Any? {
        val data = getOrCreatePlayerData(player)
        return statAccessors[stat]?.invoke(data)
    }
}