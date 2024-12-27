package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random


class TNTRainEvent : EventIntefrace {
    override val name = "TNT Rain"
    override val description = "Watch your head!"
    override val displayMaterial = Material.TNT
    override val cost = 450
    override var contributions = 0
    override val duration = 5 * 60
    override var isActive = false

    private var rainTask: BukkitTask? = null

    override fun activate() {
        isActive = true

        val ceilingRegion = RegionConfig.getRegionPositions("PVPAreaCeiling") ?: return
        val min = ceilingRegion.first
        val max = ceilingRegion.second

        val world = min.world ?: return

        rainTask = Bukkit.getScheduler().runTaskTimer(
            ElytraPVP.instance!!,
            Runnable {
                // Randomly spawn a certain number of TNT entities per tick
                repeat(5) { // Adjust the number for desired intensity
                    spawnRandomTNT(world, min, max)
                }
            }, 0L, 20L // Spawns TNT every second (20 ticks)
        )
    }

    override fun deactivate() {
        isActive = false
        rainTask?.cancel()
        rainTask = null
    }

    private fun spawnRandomTNT(world: World, min: Location, max: Location) {
        // Generate random coordinates within the region
        val x = Random.nextDouble(min.blockX.toDouble(), max.blockX + 1.0)
        val z = Random.nextDouble(min.blockZ.toDouble(), max.blockZ + 1.0)
        val y = min.y // Ceiling height

        val spawnLocation = Location(world, x, y.toDouble(), z)

        // Spawn and configure TNT
        val tnt = world.spawnEntity(spawnLocation, EntityType.PRIMED_TNT) as TNTPrimed
        tnt.fuseTicks = 80 // Adjust fuse time (ticks) for when TNT ignites after landing
    }
}