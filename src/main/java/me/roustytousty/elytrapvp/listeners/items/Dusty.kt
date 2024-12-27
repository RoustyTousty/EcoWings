package me.roustytousty.elytrapvp.listeners.items

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector

class Dusty : Listener {

    @EventHandler
    fun onDusty(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT_CLICK")) {

            val item = event.player.inventory.itemInMainHand
            if (item.type == Material.SUGAR) {
                val player = event.player
                val playerLocation = player.location
                val world = player.world

                val center = playerLocation.toVector()
                val radius = 5

                for (x in -radius..radius) {
                    for (y in -radius..radius) {
                        for (z in -radius..radius) {
                            val block = playerLocation.clone().add(Vector(x, y, z)).block
                            val blockCenter = block.location.toVector().add(Vector(0.5, 0.5, 0.5))

                            if (blockCenter.isInSphere(center, radius.toDouble())) {
                                if (block.type == Material.WHITE_WOOL) {
                                    block.type = Material.WHITE_CONCRETE_POWDER
                                }
                            }
                        }
                    }
                }

                item.amount -= 1

                world.spawnParticle(
                    Particle.CLOUD,
                    playerLocation.add(0.0, 1.0, 0.0),
                    200,
                    5.0, 5.0, 5.0,
                    0.1
                )

                world.playSound(playerLocation, Sound.BLOCK_CANDLE_EXTINGUISH, 1.0f, 1.0f)
            }
        }
    }
}