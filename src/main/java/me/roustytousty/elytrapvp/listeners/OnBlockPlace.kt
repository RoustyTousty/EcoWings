package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.RegionUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class OnBlockPlace : Listener {

    private val playerService = Services.playerService
    private val regionService = Services.regionService

    private val placableMaterials = setOf(
        Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.OAK_PLANKS,
        Material.STONE_BRICKS, Material.DEEPSLATE_BRICKS, Material.POLISHED_DEEPSLATE,
        Material.TNT, Material.AIR
    )

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val playerData = playerService.getOrCreatePlayerData(player)

        val buildmode = playerData.isBuildMode
        if (buildmode) return


        val blockLocation = event.block.location
        val isInSpawn = regionService.isInRegion(blockLocation, "spawnRegion")
        val isInBuildBufferRegion = regionService.isInRegion(blockLocation, "buildBufferRegion")

        if (isInSpawn || isInBuildBufferRegion || blockLocation.y < 85) {
            event.isCancelled = true
            MessageUtils.sendError(player, "&fYou can't place blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return
        }

        if (!placableMaterials.contains(event.block.type)) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou can only place specific blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
        }
    }
}