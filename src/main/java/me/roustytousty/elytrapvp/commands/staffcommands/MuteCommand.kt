package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MuteCommand : CommandExecutor {

    private val punishmentService = Services.punishmentService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide") && !player.hasPermission("group.mod") && !player.hasPermission("group.admin")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&cUsage: /mute <player> [time] [reason]")
            return true
        }

        val targetPlayer = Bukkit.getPlayer(args[0])
        if (targetPlayer == null) {
            MessageUtils.sendError(player, "&cPlayer not found or is offline.")
            return true
        }

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
            "Spam/Toxicity"
        }

        punishmentService.mutePlayer(targetPlayer.uniqueId, duration)

        MessageUtils.sendSuccess(player, "&aMuted &f${targetPlayer.name} &afor &f$reason&a.")
        MessageUtils.sendError(targetPlayer, "&cYou have been muted. Reason: $reason")

        return true
    }
}