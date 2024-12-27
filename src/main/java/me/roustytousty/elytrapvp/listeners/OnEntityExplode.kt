package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class OnEntityExplode : Listener {

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (event.entity.type == EntityType.PRIMED_TNT && event.blockList().size > 0) {
            val destroyed = event.blockList();
            val it = destroyed.iterator();
            while (it.hasNext()) {
                if (!RegionUtils.isLocationInRegion(it.next().location, "pvpRegion")) {
                    it.remove()
                }
            }
        }
    }
}