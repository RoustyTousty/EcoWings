package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.utility.FormatUtils
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnPlayerQuit : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        MongoDB.saveCachedData(player)

        event.quitMessage(Component.text(FormatUtils.parse("&f[&c-&f] ${player.name}")))
    }
}