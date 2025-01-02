package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.RegionUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class OnBlockBreak : Listener {

    private val breakableMaterials = setOf(
        Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL,
        Material.WHITE_CONCRETE, Material.YELLOW_CONCRETE,
        Material.WHITE_CONCRETE_POWDER
    )

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        event.isDropItems = false

        val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
        if (buildmode == true) {
            return
        }

        val blockLocation = event.block.location
        val isInBuildRegion = RegionUtils.isLocationInRegion(blockLocation, "buildRegion")
        val isInBuildBufferRegion = RegionUtils.isLocationInRegion(blockLocation, "buildBufferRegion")

        if (!isInBuildRegion || isInBuildBufferRegion) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou cant break blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        if (!breakableMaterials.contains(event.block.type)) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou cant break blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        when (event.block.type) {
            Material.ORANGE_WOOL -> {
                event.isCancelled = true
                event.block.type = Material.YELLOW_WOOL
            }
            Material.YELLOW_WOOL -> {
                event.isCancelled = true
                event.block.type = Material.WHITE_WOOL
            }
            Material.WHITE_WOOL -> {
                event.isCancelled = true
                player.inventory.addItem(event.block.drops.firstOrNull()!!)
                event.block.type = Material.AIR
            }
            Material.ORANGE_CONCRETE -> {
                event.isCancelled = true
                event.block.type = Material.YELLOW_CONCRETE
            }
            Material.YELLOW_CONCRETE -> {
                event.isCancelled = true
                event.block.type = Material.WHITE_CONCRETE
            }
            Material.WHITE_CONCRETE -> {
                event.isCancelled = true
                player.inventory.addItem(event.block.drops.firstOrNull()!!)
                event.block.type = Material.AIR
            }
            else -> {
                return
            }
        }
    }
}