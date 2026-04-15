package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.event.EventInterface
import me.roustytousty.elytrapvp.services.region.Region
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random

class TNTRainEvent : EventInterface {
    override val name = "TNT Rain"
    override val description = "Watch your head!"
    override val displayMaterial = Material.TNT
    override val cost = 550
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    private var rainTask: BukkitTask? = null
    private val pvpRegionName = "pvpRegion"

    override fun activate() {
        isActive = true

        val region = Services.regionService.get(pvpRegionName) ?: return
        val spawnY = region.getMaxY().toDouble()

        rainTask = Bukkit.getScheduler().runTaskTimer(
            ElytraPVP.instance!!,
            Runnable {
                repeat(2) {
                    if (Random.nextInt(100) < 2) {
                        targetPlayer(region, spawnY)
                    } else {
                        spawnRandom(region, spawnY)
                    }
                }
            },
            0L,
            20L
        )
    }

    override fun deactivate() {
        isActive = false
        rainTask?.cancel()
        rainTask = null
    }

    private fun targetPlayer(region: Region, y: Double) {
        val players = Bukkit.getOnlinePlayers().filter {
            region.contains(it.location)
        }

        if (players.isEmpty()) return

        val target = players.random()
        val loc = target.location

        val x = loc.x.coerceIn(region.getMinX().toDouble(), region.getMaxX().toDouble())
        val z = loc.z.coerceIn(region.getMinZ().toDouble(), region.getMaxZ().toDouble())

        spawnTNT(region.world, Location(region.world, x, y, z))
    }

    private fun spawnRandom(region: Region, y: Double) {
        val loc = region.getRandomLocation()
        spawnTNT(region.world, Location(region.world, loc.x, y, loc.z))
    }

    private fun spawnTNT(world: World, location: Location) {
        val tnt = world.spawnEntity(location, EntityType.PRIMED_TNT) as TNTPrimed
        tnt.fuseTicks = 100
    }
}