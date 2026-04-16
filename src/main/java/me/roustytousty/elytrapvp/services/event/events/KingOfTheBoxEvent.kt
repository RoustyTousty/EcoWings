package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.event.EventInterface
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.*
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random

class KingOfTheBoxEvent : EventInterface {
    override val name = "King of the Box"
    override val description = "Capture the box to earn gold!"
    override val displayMaterial = Material.BEACON
    override val cost = 600
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    private var logicTask: BukkitTask? = null
    private var moveTimer = 0
    private var currentZoneCenter: Location? = null

    private val zoneRadius = 3.0
    private val moveIntervalTicks = 2400

    private val orangeDust = Particle.DustOptions(Color.fromRGB(255, 128, 0), 1.5f)

    override fun activate() {
        isActive = true
        moveTimer = 0
        spawnNewZone()

        logicTask = Bukkit.getScheduler().runTaskTimer(ElytraPVP.instance!!, Runnable {
            val center = currentZoneCenter ?: return@Runnable

            spawnZoneParticles(center)
            processPlayersInZone(center)

            moveTimer += 10
            if (moveTimer >= moveIntervalTicks) {
                moveTimer = 0
                spawnNewZone()
            }
        }, 0L, 10L)
    }

    override fun deactivate() {
        isActive = false
        logicTask?.cancel()
        logicTask = null
        currentZoneCenter = null

        Bukkit.getOnlinePlayers().forEach {
            it.removePotionEffect(PotionEffectType.GLOWING)
        }
    }

    private fun spawnNewZone() {
        val pvpRegion = Services.regionService.get("pvpRegion") ?: return

        val x = Random.nextDouble(pvpRegion.getMinX() + zoneRadius, pvpRegion.getMaxX() - zoneRadius)
        val y = Random.nextDouble(pvpRegion.getMinY() + zoneRadius, pvpRegion.getMaxY() - zoneRadius)
        val z = Random.nextDouble(pvpRegion.getMinZ() + zoneRadius, pvpRegion.getMaxZ() - zoneRadius)

        currentZoneCenter = Location(pvpRegion.world, x, y, z)

        MessageUtils.sendMessage("The capture box has moved!")
        Bukkit.getOnlinePlayers().forEach {
            it.playSound(it.location, Sound.BLOCK_BEACON_POWER_SELECT, 1f, 1.5f)
        }
    }

    private fun processPlayersInZone(center: Location) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (isInsideZone(player.location, center)) {
                player.addPotionEffect(PotionEffectType.GLOWING.createEffect(30, 0))

                if (moveTimer % 20 == 0) {
                    Services.currencyService.giveGold(player, 1, "King of the Box")
                }
            } else {
                player.removePotionEffect(PotionEffectType.GLOWING)
            }
        }
    }

    private fun isInsideZone(loc: Location, center: Location): Boolean {
        return loc.x >= center.x - zoneRadius && loc.x <= center.x + zoneRadius &&
                loc.y >= center.y - zoneRadius && loc.y <= center.y + zoneRadius &&
                loc.z >= center.z - zoneRadius && loc.z <= center.z + zoneRadius
    }

    private fun spawnZoneParticles(center: Location) {
        val world = center.world
        val minX = center.x - zoneRadius
        val maxX = center.x + zoneRadius
        val minY = center.y - zoneRadius
        val maxY = center.y + zoneRadius
        val minZ = center.z - zoneRadius
        val maxZ = center.z + zoneRadius

        val edges = listOf(
            arrayOf(minX, minY, minZ, minX, maxY, minZ),
            arrayOf(maxX, minY, minZ, maxX, maxY, minZ),
            arrayOf(minX, minY, maxZ, minX, maxY, maxZ),
            arrayOf(maxX, minY, maxZ, maxX, maxY, maxZ),

            arrayOf(minX, minY, minZ, maxX, minY, minZ),
            arrayOf(minX, minY, maxZ, maxX, minY, maxZ),
            arrayOf(minX, minY, minZ, minX, minY, maxZ),
            arrayOf(maxX, minY, minZ, maxX, minY, maxZ),

            arrayOf(minX, maxY, minZ, maxX, maxY, minZ),
            arrayOf(minX, maxY, maxZ, maxX, maxY, maxZ),
            arrayOf(minX, maxY, minZ, minX, maxY, maxZ),
            arrayOf(maxX, maxY, minZ, maxX, maxY, maxZ)
        )

        edges.forEach { e -> drawParticleLine(world, e[0], e[1], e[2], e[3], e[4], e[5]) }
    }

    private fun drawParticleLine(world: World, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        val distance = Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0) + Math.pow(z2 - z1, 2.0))
        val points = (distance * 5).toInt()

        for (i in 0..points) {
            val ratio = i.toDouble() / points
            val x = x1 + (x2 - x1) * ratio
            val y = y1 + (y2 - y1) * ratio
            val z = z1 + (z2 - z1) * ratio

            world.spawnParticle(Particle.REDSTONE, x, y, z, 1, 0.0, 0.0, 0.0, 0.0, orangeDust, true)
        }
    }
}