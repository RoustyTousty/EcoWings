package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.gui.stats.PlayerStatsMenu
import me.roustytousty.elytrapvp.utility.RegionUtils
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class OnPlayerInteractEntity : Listener {

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEntityEvent) {

        if (event.rightClicked !is Player) {
            return
        }

        val clickedPlayer = event.rightClicked as Player
        val player = event.player

        if (CitizensAPI.getNPCRegistry().isNPC(clickedPlayer)) {
            return
        }

        if (!RegionUtils.isLocationInRegion(clickedPlayer.location, "spawnRegion") && !RegionUtils.isLocationInRegion(player.location, "spawnRegion")) {
            return
        }

        PlayerStatsMenu.openInventory(player, clickedPlayer)
    }
}