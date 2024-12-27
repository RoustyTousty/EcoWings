package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
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
    private val ceilingRegionName = "PVPAreaCeiling"
    private val pvpRegionName = "pvpRegion"

    override fun activate() {
        isActive = true

        val ceilingRegion = RegionConfig.getRegionPositions(ceilingRegionName) ?: return
        val min = ceilingRegion.first
        val max = ceilingRegion.second

        val world = min.world ?: return

        rainTask = Bukkit.getScheduler().runTaskTimer(
            ElytraPVP.instance!!,
            Runnable {
                repeat(2) {
                    if (Random.nextInt(100) < 2) {
                        targetPlayerInRegion(world, min, max)
                    } else {
                        spawnRandomTNT(world, min, max)
                    }
                }
            }, 0L, 20L
        )
    }

    override fun deactivate() {
        isActive = false
        rainTask?.cancel()
        rainTask = null
    }

    private fun targetPlayerInRegion(world: World, min: Location, max: Location) {
        val playersInRegion = Bukkit.getOnlinePlayers().filter { player ->
            RegionUtils.isLocationInRegion(player.location, pvpRegionName)
        }

        if (playersInRegion.isNotEmpty()) {
            val targetPlayer = playersInRegion.random()
            val playerLocation = targetPlayer.location

            val x = playerLocation.x.coerceIn(min.blockX.toDouble() + 0.5, max.blockX.toDouble() + 0.5)
            val z = playerLocation.z.coerceIn(min.blockZ.toDouble() + 0.5, max.blockZ.toDouble() + 0.5)
            val targetLocation = Location(world, x, min.y, z)

            spawnTNT(world, targetLocation)
        }
    }

    private fun spawnRandomTNT(world: World, min: Location, max: Location) {
        val x = Random.nextDouble(min.blockX.toDouble() + 0.5, max.blockX + 0.5).coerceIn(min.blockX.toDouble() + 0.5, max.blockX.toDouble() + 0.5)
        val z = Random.nextDouble(min.blockZ.toDouble() + 0.5, max.blockZ + 0.5).coerceIn(min.blockZ.toDouble() + 0.5, max.blockZ.toDouble() + 0.5)
        val y = min.y

        val spawnLocation = Location(world, x, y, z)
        spawnTNT(world, spawnLocation)
    }

    private fun spawnTNT(world: World, location: Location) {
        val tnt = world.spawnEntity(location, EntityType.PRIMED_TNT) as TNTPrimed
        tnt.fuseTicks = 80
    }
}