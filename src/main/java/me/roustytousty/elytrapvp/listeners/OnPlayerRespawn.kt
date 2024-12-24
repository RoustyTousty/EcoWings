package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.kit.KitService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class OnPlayerRespawn : Listener {

    private val kitService = KitService()

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player

        kitService.giveKit(player)

        MessageUtils.sendTitle(player, "&c&lDeath", "&7Try harder next time.", 5, 30, 5)

        event.player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))
    }
}