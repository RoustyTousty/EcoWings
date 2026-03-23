package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import org.bukkit.Bukkit
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
    override val displayMaterial = Material.SCULK
    override val cost = 200
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    private val BOUNCE_VELOCITY = 2.5
    private val regionName = "voidlessBlock"

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerMove(event: PlayerMoveEvent) {
            val region = Services.regionService.get(regionName) ?: return
            val player = event.player

            if (player.location.y < region.getMinY()) {
                player.velocity = Vector(0.0, BOUNCE_VELOCITY, 0.0)
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
        val region = Services.regionService.get(regionName) ?: return
        val world = region.world
        val y = region.getMinY().toDouble()

        particleTask = Bukkit.getScheduler().runTaskTimer(
            ElytraPVP.instance!!,
            Runnable {
                region.forEachBlock {
                    if (it.y == y.toInt()) {
                        world.spawnParticle(
                            Particle.REDSTONE,
                            it.location.clone().add(0.5, 0.0, 0.5),
                            1,
                            Particle.DustOptions(org.bukkit.Color.ORANGE, 1.0f)
                        )
                    }
                }
            },
            0L,
            10L
        )
    }

    private fun removeParticleFloor() {
        particleTask?.cancel()
        particleTask = null
    }
}