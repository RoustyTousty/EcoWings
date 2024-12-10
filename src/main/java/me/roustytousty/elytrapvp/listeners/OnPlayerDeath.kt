package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils.parse
import me.roustytousty.elytrapvp.utility.bounty.BountyManager
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class OnPlayerDeath : Listener {


    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = victim.killer as Player

        if (killer != null) {
            handleKillerActions(killer, victim)
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!"))
            )
        } else {
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fhas died!"))
            )
        }

        handleVictimActions(victim)
    }

    private fun handleKillerActions(killer: Player, victim: Player) {
        val kills = CacheConfig.getplrVal(killer, "kills") as? Int ?: 0
        val killstreak = CacheConfig.getplrVal(killer, "killstreak") as? Int ?: 0
        val gold = CacheConfig.getplrVal(killer, "gold") as? Int ?: 0

        val newKillstreak = killstreak + 1

        CacheConfig.setplrVal(killer, "kills", kills + 1)
        CacheConfig.setplrVal(killer, "killstreak", newKillstreak)
        CacheConfig.setplrVal(killer, "gold", gold + 10)

        killer.health = (killer.health + 3).coerceAtMost(killer.maxHealth)
        killer.sendActionBar(parse("&6+10g"))
        killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

        if (newKillstreak >= 5) {
            BountyManager().applyBounty(killer, newKillstreak * 5)
        }
    }

    private fun handleVictimActions(victim: Player) {
        val deaths = CacheConfig.getplrVal(victim, "deaths") as? Int ?: 0

        CacheConfig.setplrVal(victim, "deaths", deaths + 1)
        CacheConfig.setplrVal(victim, "killstreak", 0)

        BountyManager().removeBounty(victim)
    }
}
