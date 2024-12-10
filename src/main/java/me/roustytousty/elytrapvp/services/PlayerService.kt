package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.services.bounty.BountyService
import me.roustytousty.elytrapvp.utility.MessageUtils.sendActionBar
import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerService {

    fun handleKillActions(player: Player) {
        val kills = CacheConfig.getplrVal(player, "kills") as? Int ?: 0
        val killstreak = CacheConfig.getplrVal(player, "killstreak") as? Int ?: 0
        val gold = CacheConfig.getplrVal(player, "gold") as? Int ?: 0

        val newKillstreak = killstreak + 1

        CacheConfig.setplrVal(player, "kills", kills + 1)
        CacheConfig.setplrVal(player, "killstreak", newKillstreak)
        CacheConfig.setplrVal(player, "gold", gold + 10)

        player.health = (player.health + 3).coerceAtMost(player.maxHealth)
        sendActionBar(player, "&6+10g")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

        if (newKillstreak >= 10) {
            BountyService().applyBounty(player, newKillstreak * 5)
        }
    }

    fun handleDeathActions(player: Player) {
        val deaths = CacheConfig.getplrVal(player, "deaths") as? Int ?: 0

        CacheConfig.setplrVal(player, "deaths", deaths + 1)
        CacheConfig.setplrVal(player, "killstreak", 0)

        BountyService().removeBounty(player)
    }
}