package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector

class VoidlessEvent : EventIntefrace {
    override val name = "Voidless"
    override val description = "No void, no problem."
    override val displayManetial = Material.SCULK
    override val cost = 200
    override var contributions = 0
    override val duration = 5 * 60
    override var isActive = false

    private val BOUNCE_VELOCITY = 2.5

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerMove(event: PlayerMoveEvent) {
            val player = event.player
            val voidlessRegion = RegionConfig.getRegionPositions("voidlessBlock") ?: return
            val minY = voidlessRegion.first.y

            if (player.location.y < minY) {
                val bounceVelocity = Vector(0.0, BOUNCE_VELOCITY, 0.0)
                player.velocity = bounceVelocity
            }
        }
    }

    private var particleTask: BukkitTask? = null

    override fun activate() {
        isActive = true
        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)
        createParticleFloor()
    }

    override fun deactivate() {
        isActive = false
        PlayerMoveEvent.getHandlerList().unregister(listener)
        removeParticleFloor()
    }


    private fun createParticleFloor() {
        val voidlessRegion = RegionConfig.getRegionPositions("voidlessBlock") ?: return
        val min = voidlessRegion.first
        val max = voidlessRegion.second

        val world = min.world ?: return

        particleTask = Bukkit.getScheduler().runTaskTimer(
            ElytraPVP.instance!!,
            Runnable {
            for (x in min.blockX..max.blockX) {
                for (z in min.blockZ..max.blockZ) {
                    val location = Location(world, x.toDouble() + 0.5, min.y, z.toDouble() + 0.5)
                    world.spawnParticle(Particle.REDSTONE, location, 1, Particle.DustOptions(org.bukkit.Color.ORANGE, 1.0f))
                }
            }
        }, 0L, 10L)
    }

    private fun removeParticleFloor() {
        particleTask?.cancel()
        particleTask = null
    }
}