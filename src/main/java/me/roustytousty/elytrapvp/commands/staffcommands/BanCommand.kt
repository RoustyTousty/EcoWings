package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BanCommand : CommandExecutor {

    private val punishmentService = Services.punishmentService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide") && !player.hasPermission("group.mod") && !player.hasPermission("group.admin") && !player.hasPermission("group.owner")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: &6/ban <player> [time] [reason]")
            return true
        }

        val targetName = args[0]
        var duration = punishmentService.getMaxDurationMillis(player)
        var reasonStartIndex = 1

        if (args.size > 1) {
            val parsedTime = punishmentService.parseDuration(args[1])
            if (parsedTime != null) {
                val maxLimit = punishmentService.getMaxDurationMillis(player)
                duration = if (parsedTime > maxLimit) maxLimit else parsedTime
                reasonStartIndex = 2
            }
        }

        val reason = if (args.size > reasonStartIndex) {
            args.copyOfRange(reasonStartIndex, args.size).joinToString(" ")
        } else {
            "Rule violation"
        }

        punishmentService.banPlayer(targetName, duration, reason, player.name)

        val targetPlayer = Bukkit.getPlayerExact(targetName)
        MessageUtils.sendError(targetPlayer!!, "&fYou have been banned. \n&7Reason: $reason")

        MessageUtils.sendSuccess(player, "&fSuccessfully banned &6$targetName &ffor &6$reason")
        return true
    }
}