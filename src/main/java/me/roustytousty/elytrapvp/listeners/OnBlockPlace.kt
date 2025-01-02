package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.RegionUtils
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class OnBlockPlace : Listener {

    private val placableMaterials = setOf(
        Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL,
        Material.WHITE_CONCRETE, Material.YELLOW_CONCRETE,
        Material.TNT
    )

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
        if (buildmode == true) {
            return
        }

        val blockLocation = event.block.location
        val isInBuildRegion = RegionUtils.isLocationInRegion(blockLocation, "buildRegion")
        val isInBuildBufferRegion = RegionUtils.isLocationInRegion(blockLocation, "buildBufferRegion")

        if (!isInBuildRegion || isInBuildBufferRegion) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou can't place blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        if (!placableMaterials.contains(event.block.type)) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou can only place specific blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
        }
    }
}