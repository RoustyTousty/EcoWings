package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
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

        if (punishmentService.isMuted(player.uniqueId)) {
            MessageUtils.sendError(player, "&cYou are currently muted and cannot speak.")
            event.isCancelled = true
            return
        }

        val prefix = LuckPermsUtils.getPrefix(player)
        event.format = parse("$prefix &f${player.name}&f: &f%2\$s")
    }
}