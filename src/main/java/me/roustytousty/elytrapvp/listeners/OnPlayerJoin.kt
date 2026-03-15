package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.scoreboard.ScoreboardService
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.kit.KitService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OnPlayerJoin : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        Services.playerService.getOrCreatePlayerData(player)

        player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))
        event.joinMessage(Component.text(parse("&f[&a+&f] ${player.name}")))

        Services.scoreboardService.create(player)
    }
}