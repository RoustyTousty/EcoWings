package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class OnPlayerDeath : Listener {

    private val playerService = Services.playerService

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = victim.killer

        if (killer is Player) {

            calculateKillerStats(killer)

            killer.health = (killer.health + 3).coerceAtMost(killer.maxHealth)

            killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

            event.deathMessage(
                Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!"))
            )
        } else {
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fhas died!"))
            )
        }

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
        killerData.gold += 10
        MessageUtils.sendActionBar(player, "&6+10g")
    }

    private fun calculateVictimStats(player: Player) {
        val victimData = playerService.getOrCreatePlayerData(player)
        victimData.deaths += 1
        victimData.killstreak = 0
    }
}
