package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.LuckPermsUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class OnAsyncPlayerChat : Listener {

    private val punishmentService = Services.punishmentService

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val activeMute = Services.punishmentService.getActiveMute(player.uniqueId)

        if (activeMute != null) {
            val timeStr = FormatUtils.formatDuration(activeMute.durationMillis)
            MessageUtils.sendError(player, "&fYou are currently muted by &6${activeMute.issuer} &ffor (&6$timeStr&f). &fReason: &7${activeMute.reason}")
            event.isCancelled = true
            return
        }

        val prefix = LuckPermsUtils.getPrefix(player)
        event.format = parse("$prefix &f${player.name}&f: &f%2\$s")
    }
}