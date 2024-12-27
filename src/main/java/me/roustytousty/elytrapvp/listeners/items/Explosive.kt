package me.roustytousty.elytrapvp.listeners.items

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class Explosive : Listener {

    @EventHandler
    fun OnExplosive(event: BlockPlaceEvent) {
        val block = event.block
        val world = block.world
        val location = block.location

        if (block.type == Material.TNT) {
            block.type = Material.AIR
            val tnt = world.spawnEntity(Location(world, location.x + 0.5, location.y + 0.5, location.z + 0.5), EntityType.PRIMED_TNT) as TNTPrimed
            tnt.fuseTicks = 50
            world.playSound(location, Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f)
        }
    }
}