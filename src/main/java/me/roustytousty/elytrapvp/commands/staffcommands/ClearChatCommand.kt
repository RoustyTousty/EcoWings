package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearChatCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        repeat(300) { Bukkit.broadcast(net.kyori.adventure.text.Component.empty()) }

        MessageUtils.sendAnnouncement("&fChat was cleared by &6${player.name}&f.")
        return true
    }
}