package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.configs.RegionConfig
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

class OnPlayerMove : Listener {

    private val eventService = Services.eventService
    private val combatService = Services.combatService

    private val wallShown = mutableSetOf<UUID>()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val loc = player.location
        val from = event.from
        val to = event.to

        if (loc.y <= 85 && !eventService.isEventActive("Voidless")) {
            val isInPVPRegion = RegionUtils.isLocationInRegion(loc, "pvpRegion")
            if (!isInPVPRegion) {
                player.damage(40.0)
            }
        }

        val (pos1, pos2) = RegionConfig.getRegionPositions("spawnEntrance") ?: return

        val inEntrance = RegionUtils.isLocationInRegion(loc, "spawnEntrance")

        val enteringEntrance =
            !RegionUtils.isLocationInRegion(from, "spawnEntrance") &&
                    RegionUtils.isLocationInRegion(to, "spawnEntrance")

        val nearEntrance = isNearRegion(player, pos1, pos2, 3.0)

        val inCombat = combatService.isInCombat(player)

        if (inCombat && enteringEntrance) {
            event.isCancelled = true
            showWall(player)
            return
        }

        if (inCombat && nearEntrance) {
            showWall(player)
        } else {
            clearWall(player)
        }
    }

    private fun isNearRegion(player: Player, pos1: org.bukkit.Location, pos2: org.bukkit.Location, distance: Double): Boolean {
        val loc = player.location

        val (minX, maxX) = listOf(pos1.x, pos2.x).sorted()
        val (minY, maxY) = listOf(pos1.y, pos2.y).sorted()
        val (minZ, maxZ) = listOf(pos1.z, pos2.z).sorted()

        val closestX = loc.x.coerceIn(minX, maxX)
        val closestY = loc.y.coerceIn(minY, maxY)
        val closestZ = loc.z.coerceIn(minZ, maxZ)

        val closestPoint = org.bukkit.Location(loc.world, closestX, closestY, closestZ)

        return loc.distance(closestPoint) <= distance
    }

    private fun showWall(player: Player) {
        if (wallShown.contains(player.uniqueId)) return

        val (pos1, pos2) = RegionConfig.getRegionPositions("spawnEntrance") ?: return
        val world = player.world

        val (minX, maxX) = listOf(pos1.blockX, pos2.blockX).sorted()
        val (minY, maxY) = listOf(pos1.blockY, pos2.blockY).sorted()
        val (minZ, maxZ) = listOf(pos1.blockZ, pos2.blockZ).sorted()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val loc = org.bukkit.Location(world, x.toDouble(), y.toDouble(), z.toDouble())

                    player.sendBlockChange(
                        loc,
                        Material.RED_STAINED_GLASS.createBlockData()
                    )
                }
            }
        }

        wallShown.add(player.uniqueId)
    }
    
    private fun clearWall(player: Player) {
        if (!wallShown.contains(player.uniqueId)) return

        val (pos1, pos2) = RegionConfig.getRegionPositions("spawnEntrance") ?: return
        val world = player.world

        val (minX, maxX) = listOf(pos1.blockX, pos2.blockX).sorted()
        val (minY, maxY) = listOf(pos1.blockY, pos2.blockY).sorted()
        val (minZ, maxZ) = listOf(pos1.blockZ, pos2.blockZ).sorted()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val loc = org.bukkit.Location(world, x.toDouble(), y.toDouble(), z.toDouble())

                    val realBlock = world.getBlockAt(loc)
                    player.sendBlockChange(loc, realBlock.blockData)
                }
            }
        }

        wallShown.remove(player.uniqueId)
    }
}