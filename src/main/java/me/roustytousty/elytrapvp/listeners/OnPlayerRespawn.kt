package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.KitUtils
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class OnPlayerRespawn : Listener {

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        event.player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))

        KitUtils.givePlayerKit(player)

        MessageUtils.sendTitle(player, "&c&lDeath", "&7Try harder next time.", 5, 30, 5)
    }
}