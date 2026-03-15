package me.roustytousty.elytrapvp.services.player

import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerService(
    private val repository: PlayerRepository,
    private val cache: PlayerCache
) {

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

        repository.savePlayer(data)

        cache.remove(uuid)
    }







    fun handleKillAction(player: Player) {
        val playerData = getOrCreatePlayerData(player)

        playerData.killstreak += 1
        playerData.kills += 1
        playerData.gold += 10

        player.health = (player.health + 3).coerceAtMost(player.maxHealth)
        MessageUtils.sendActionBar(player, "&6+10g")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }

    fun handleDeathAction(player: Player) {
        val playerData = getOrCreatePlayerData(player)

        playerData.deaths += 1
        playerData.killstreak = 0
    }

    fun handleRebirthAction(Player: Player) {

    }
}