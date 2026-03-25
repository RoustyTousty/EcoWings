package me.roustytousty.elytrapvp.listeners

import org.bukkit.Material
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class OnEntityExplode : Listener {

    private val blockWhitelist = setOf(
        Material.WHITE_WOOL,
        Material.LIGHT_GRAY_WOOL,
        Material.OAK_PLANKS,
        Material.STONE_BRICKS,
        Material.DEEPSLATE_BRICKS
    )

    @EventHandler
    fun onExplode(event: EntityExplodeEvent) {
        event.blockList().removeIf { block ->
            block.type !in blockWhitelist
        }
    }
}