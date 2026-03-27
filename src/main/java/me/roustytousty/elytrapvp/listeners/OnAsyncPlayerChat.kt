package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.LuckPermsUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class OnAsyncPlayerChat : Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val prefix = LuckPermsUtils.getPrefix(event.player)
        event.format = parse("$prefix &f${player.name}&f: &f%2\$s")
    }
}