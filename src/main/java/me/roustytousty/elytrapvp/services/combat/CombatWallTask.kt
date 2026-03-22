package me.roustytousty.elytrapvp.services.combat

import me.roustytousty.elytrapvp.services.region.RegionService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.ceil

class CombatWallTask(
    private val regionService: RegionService,
    private val combatService: CombatService,
    private val plugin: JavaPlugin
) : BukkitRunnable() {

    private val wallShown = mutableSetOf<UUID>()

    override fun run() {
        val region = regionService.get("spawnEntrance") ?: return
        val (pos1, pos2) = region
        val world = pos1.world ?: return

        val (minX, maxX) = listOf(pos1.blockX, pos2.blockX).sorted()
        val (minY, maxY) = listOf(pos1.blockY, pos2.blockY).sorted()
        val (minZ, maxZ) = listOf(pos1.blockZ, pos2.blockZ).sorted()

        for (player in Bukkit.getOnlinePlayers()) {
            val remaining = combatService.getRemainingTime(player)

            if (remaining > 0) {
                val seconds = ceil(remaining / 1000.0).toInt()
                MessageUtils.sendActionBar(player, "&fIn combat: &6${seconds}&fs")

                showWall(player, world, minX, maxX, minY, maxY, minZ, maxZ)

                wallShown.add(player.uniqueId)
            } else {
                if (wallShown.remove(player.uniqueId)) {
                    clearWall(player, world, minX, maxX, minY, maxY, minZ, maxZ)
                }
            }
        }
    }

    private fun showWall(
        player: Player,
        world: World,
        minX: Int, maxX: Int,
        minY: Int, maxY: Int,
        minZ: Int, maxZ: Int
    ) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val loc = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
                    player.sendBlockChange(loc, Material.RED_STAINED_GLASS.createBlockData())
                }
            }
        }
    }

    private fun clearWall(
        player: Player,
        world: World,
        minX: Int, maxX: Int,
        minY: Int, maxY: Int,
        minZ: Int, maxZ: Int
    ) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val loc = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
                    val realBlock = world.getBlockAt(loc)
                    player.sendBlockChange(loc, realBlock.blockData)
                }
            }
        }
    }

    fun start() {
        this.runTaskTimer(plugin, 0L, 5L)
    }
}