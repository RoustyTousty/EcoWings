package me.roustytousty.elytrapvp.services.player

import me.roustytousty.elytrapvp.configs.CacheConfig
import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.services.bounty.BountyService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerService(
    private val repository: PlayerRepository,
    private val cache: PlayerCache
) {

    fun getOrCreatePlayer(player: Player): PlayerData {
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

    fun handleKillAction(player: Player) {
        val kills = CacheConfig.getplrVal(player, "kills") as? Int ?: 0
        val killstreak = CacheConfig.getplrVal(player, "killstreak") as? Int ?: 0
        val gold = CacheConfig.getplrVal(player, "gold") as? Int ?: 0

        val newKillstreak = killstreak + 1

        CacheConfig.setplrVal(player, "kills", kills + 1)
        CacheConfig.setplrVal(player, "killstreak", newKillstreak)
        CacheConfig.setplrVal(player, "gold", gold + 10)

        player.health = (player.health + 3).coerceAtMost(player.maxHealth)
        MessageUtils.sendActionBar(player, "&6+10g")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

        if (newKillstreak >= 10) {
            BountyService().applyBounty(player, newKillstreak * 5)
        }
    }

    fun handleDeathAction(player: Player) {
        val deaths = CacheConfig.getplrVal(player, "deaths") as? Int ?: 0

        CacheConfig.setplrVal(player, "deaths", deaths + 1)
        CacheConfig.setplrVal(player, "killstreak", 0)

        BountyService().removeBounty(player)
    }

    fun handleRebirthAction(Player: Player) {

    }
}