package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.RegionUtils
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class OnBlockPlace : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
        if (buildmode == true) {
            return
        }

        val blockLocation = event.block.location
        val isInPVPRegion = RegionUtils.isLocationInRegion(blockLocation, "pvpRegion")
        val isInPVPBufferRegion = RegionUtils.isLocationInRegion(blockLocation, "pvpBufferRegion")

        if (!isInPVPRegion || isInPVPBufferRegion) {
            event.isCancelled = true
            MessageUtils.sendMessage(player, "&c&lEcoWings &8| &fYou cant place blocks here!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
        }
    }
}