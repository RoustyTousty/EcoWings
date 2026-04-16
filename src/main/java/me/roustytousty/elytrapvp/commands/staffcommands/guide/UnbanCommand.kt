package me.roustytousty.elytrapvp.commands.staffcommands.guide

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnbanCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: &6/unban <player>")
            return true
        }

        val targetName = args[0]
        val uuid = Bukkit.getOfflinePlayer(targetName).uniqueId

        if (Services.punishmentService.unbanPlayer(uuid)) {
            MessageUtils.sendSuccess(player, "&fSuccessfully unbanned &6$targetName&f.")
        } else {
            MessageUtils.sendError(player, "&fThis player does not have an active ban.")
        }
        return true
    }
}