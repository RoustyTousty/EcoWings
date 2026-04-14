package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KickCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide") && !player.hasPermission("group.mod")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: &6/kick <player> [reason]")
            return true
        }

        val targetPlayer = Bukkit.getPlayer(args[0])
        if (targetPlayer == null) {
            MessageUtils.sendError(player, "&fPlayer not found or is offline.")
            return true
        }

        val reason = if (args.size > 1) {
            args.copyOfRange(1, args.size).joinToString(" ")
        } else {
            "You have been kicked from the server."
        }

        MessageUtils.sendError(targetPlayer, "&fYou have been kicked. \n&7Reason: $reason")
        MessageUtils.sendSuccess(player, "&fSuccessfully kicked &6${targetPlayer.name}&f!")

        targetPlayer.kick()

        return true
    }
}