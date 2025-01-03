package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import net.kyori.adventure.text.Component
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
            PlayerService().handleKillAction(killer)
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!"))
            )
        } else {
            event.deathMessage(
                Component.text(parse("&6${victim.name} &fhas died!"))
            )
        }

        PlayerService().handleDeathAction(victim)
    }
}
