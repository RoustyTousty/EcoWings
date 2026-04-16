package me.roustytousty.elytrapvp.listeners.items.utility

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector

class Normalizer : Listener {

    @EventHandler
    fun onDusty(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT_CLICK")) {

            val item = event.player.inventory.itemInMainHand

            if (item.type == Material.GUNPOWDER) {
                val player = event.player
                val playerLocation = player.location
                val world = player.world

                val center = playerLocation.toVector()
                val radius = 10

                for (x in -radius..radius) {
                    for (y in -radius..radius) {
                        for (z in -radius..radius) {
                            val block = playerLocation.clone().add(Vector(x, y, z)).block
                            val blockCenter = block.location.toVector().add(Vector(0.5, 0.5, 0.5))

                            if (blockCenter.isInSphere(center, radius.toDouble())) {
                                if (block.type == Material.WHITE_CONCRETE_POWDER) {
                                    block.type = Material.WHITE_WOOL
                                }
                            }
                        }
                    }
                }

                item.amount -= 1

                world.spawnParticle(
                    Particle.LARGE_SMOKE,
                    playerLocation.add(0.0, 1.0, 0.0),
                    300,
                    10.0, 10.0, 10.0,
                    0.1
                )

                world.playSound(playerLocation, Sound.BLOCK_CANDLE_EXTINGUISH, 2.0f, 1.0f)
            }
        }
    }
}