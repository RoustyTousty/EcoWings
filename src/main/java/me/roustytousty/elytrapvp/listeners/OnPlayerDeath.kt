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

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = victim.killer

        if (killer is Player) {
            val killerData = Services.playerService.getOrCreatePlayerData(killer)

            killerData.killstreak += 1
            killerData.kills += 1
            killerData.gold += 10

            killer.health = (killer.health + 3).coerceAtMost(killer.maxHealth)
            MessageUtils.sendActionBar(killer, "&6+10g")
            killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

            event.deathMessage(
                Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!"))
            )
        } else {
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fhas died!"))
            )
        }

        val victimData = Services.playerService.getOrCreatePlayerData(victim)
        victimData.deaths += 1
        victimData.killstreak = 0
    }
}
