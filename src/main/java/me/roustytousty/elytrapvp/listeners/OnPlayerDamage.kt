package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnPlayerDamage : Listener {

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        val attacker = event.damager as? Player ?: return
        val victim = event.entity as? Player ?: return

        if (!RegionUtils.isLocationInRegion(attacker.location, "pvpRegion") || !RegionUtils.isLocationInRegion(victim.location, "pvpRegion")) {
            event.isCancelled = true
        }
    }
}