package me.roustytousty.elytrapvp.services.combat

import me.roustytousty.elytrapvp.services.region.Region
import me.roustytousty.elytrapvp.services.region.RegionService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Material
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

        for (player in Bukkit.getOnlinePlayers()) {
            val remaining = combatService.getRemainingTime(player)

            if (remaining > 0) {
                val seconds = ceil(remaining / 1000.0).toInt()
                MessageUtils.sendActionBar(player, "&fIn combat: &6${seconds}&fs")

                showWall(player, region)
                wallShown.add(player.uniqueId)
            } else {
                if (wallShown.remove(player.uniqueId)) {
                    clearWall(player, region)
                }
            }
        }
    }

    private fun showWall(player: Player, region: Region) {
        region.forEachBlock {
            player.sendBlockChange(it.location, Material.BROWN_STAINED_GLASS.createBlockData())
        }
    }

    private fun clearWall(player: Player, region: Region) {
        region.forEachBlock {
            player.sendBlockChange(it.location, it.blockData)
        }
    }

    fun start() {
        this.runTaskTimer(plugin, 0L, 5L)
    }
}