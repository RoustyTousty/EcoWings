package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.SoundUtils
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class OnPlayerDeath : Listener {

    private val playerService = Services.playerService
    private val currencyService = Services.currencyService
    private val combatService = Services.combatService

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = combatService.getLastAttacker(victim)

        if (killer != null) {
            calculateKillerStats(killer)

            val healAmount = if (combatService.hasRespawnProtection(killer)) 6.0 else 1.0

            killer.health = (killer.health + healAmount).coerceAtMost(killer.maxHealth)
            SoundUtils.playSuccess(killer)

            event.deathMessage(Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!")))
        } else {
            event.deathMessage(Component.text(parse("&6${victim.name} &fhas died!")))
        }

        combatService.clear(victim)
        calculateVictimStats(victim)
    }

    private fun calculateKillerStats(player: Player) {
        val killerData = playerService.getOrCreatePlayerData(player)

        // KILLSTREAK
        killerData.killstreak += 1
        if (killerData.killstreak > killerData.recordKillstreak) {
            killerData.recordKillstreak = killerData.killstreak
        }

        // KILLS
        killerData.kills += 1

        // GOLD
        currencyService.giveGold(player, 10, "KILL")
    }

    private fun calculateVictimStats(player: Player) {
        val victimData = playerService.getOrCreatePlayerData(player)
        victimData.deaths += 1
        victimData.killstreak = 0
    }
}
