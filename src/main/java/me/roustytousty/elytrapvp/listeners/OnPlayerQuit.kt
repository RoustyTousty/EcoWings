package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnPlayerQuit : Listener {

    private val playerService = Services.playerService
    private val combatService = Services.combatService

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        playerService.saveAndUnloadPlayerData(player)

        combatService.clear(player)

        event.quitMessage(Component.text(FormatUtils.parse("&f[&c-&f] ${player.name}")))
    }
}