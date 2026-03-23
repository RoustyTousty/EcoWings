package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class OnBlockBreak : Listener {

    private val playerService = Services.playerService
    private val regionService = Services.regionService
    private val goldSpawnService = Services.goldSpawnService
    private val currencyService = Services.currencyService


    private val breakableMaterials = setOf(
        Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.OAK_PLANKS,
        Material.STONE_BRICKS, Material.DEEPSLATE_BRICKS, Material.POLISHED_DEEPSLATE,
        Material.WHITE_CONCRETE_POWDER,
        Material.RAW_GOLD_BLOCK
    )

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        val playerData = playerService.getOrCreatePlayerData(player)

        event.isDropItems = false

        val buildmode = playerData.isBuildMode
        if (buildmode) {
            return
        }

        val block = event.block
        val blockLocation = block.location
        val isInSpawn = regionService.isInRegion(blockLocation, "spawnRegion")
        val isInBuildBufferRegion = regionService.isInRegion(blockLocation, "buildBufferRegion")

        if (isInSpawn || isInBuildBufferRegion) {
            event.isCancelled = true
            MessageUtils.sendError(player, "&fYou can't break blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return
        }

        if (!breakableMaterials.contains(block.type)) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&fYou can only break specific blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        if (goldSpawnService.isGoldBlock(blockLocation)) {
            goldSpawnService.removeGoldBlock(blockLocation)

            currencyService.giveGold(player, 20, "MINE")

            player.playSound(player, Sound.BLOCK_BEACON_DEACTIVATE, 2f, 0.8f)
            return
        }
    }
}