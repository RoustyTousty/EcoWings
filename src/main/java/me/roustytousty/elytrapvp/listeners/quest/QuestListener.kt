package me.roustytousty.elytrapvp.listeners.other

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.quest.QuestType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent

class QuestListener : Listener {

    private val questService = Services.questService

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(e: PlayerDeathEvent) {
        val victim = e.entity

        questService.handleProgress(victim, QuestType.DEATHS, 1)

        val killer = victim.killer ?: return
        questService.handleProgress(killer, QuestType.KILL_PLAYERS, 1)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlace(e: BlockPlaceEvent) {
        questService.handleProgress(e.player, QuestType.PLACE_BLOCKS, 1, e.block.type)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBreak(e: BlockBreakEvent) {
        questService.handleProgress(e.player, QuestType.BREAK_BLOCKS, 1, e.block.type)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onFly(e: PlayerMoveEvent) {
        if (!e.player.isGliding) return
        if (e.from.blockX == e.to.blockX && e.from.blockZ == e.to.blockZ) return
        val dist = e.from.distance(e.to)
        if (dist > 0) {
            questService.handleProgress(e.player, QuestType.FLY_DISTANCE, dist.toInt())
        }
    }
}