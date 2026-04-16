package me.roustytousty.elytrapvp.services.cosmetic

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

class CosmeticBlockParticleTask : BukkitRunnable() {

    private var ticks = 0.0

    override fun run() {
        val region = Services.regionService.get("cosmeticBlockRegion") ?: return
        val world = region.world

        val centerX = (region.getMinX() + region.getMaxX()) / 2.0 + 0.5
        val centerY = (region.getMinY() + region.getMaxY()) / 2.0 + 0.5
        val centerZ = (region.getMinZ() + region.getMaxZ()) / 2.0 + 0.5
        val baseLoc = Location(world, centerX, centerY, centerZ)

        world.spawnParticle(Particle.PORTAL, baseLoc, 2, 0.3, 0.3, 0.3, 0.02)

        ticks += 0.2
        val x = 0.6 * cos(ticks)
        val z = 0.6 * sin(ticks)

        val haloLoc = baseLoc.clone().add(x, 0.7, z)
        world.spawnParticle(Particle.END_ROD, haloLoc, 1, 0.0, 0.0, 0.0, 0.0)
    }
}