package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.model.PunishmentType
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
        if (!player.hasPermission("group.guide")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: &6/ban <player> [time] [reason]")
            return true
        }

        val targetName = args[0]
        var duration = -1L
        var reasonStartIndex = 1

        if (args.size > 1) {
            val parsedTime = punishmentService.parseDuration(args[1])
            if (parsedTime != null) {
                val maxLimit = punishmentService.getMaxDurationMillis(player)
                duration = if (parsedTime > maxLimit && maxLimit != Long.MAX_VALUE) maxLimit else parsedTime
                reasonStartIndex = 2
            } else {
                duration = punishmentService.getMaxDurationMillis(player)
                reasonStartIndex = 1
            }
        } else {
            duration = punishmentService.getMaxDurationMillis(player)
        }

        val reason = if (args.size > reasonStartIndex) {
            args.copyOfRange(reasonStartIndex, args.size).joinToString(" ")
        } else {
            "Rule violation"
        }

        val targetPlayer = Bukkit.getPlayerExact(targetName)
        val targetUUID = targetPlayer?.uniqueId ?: Bukkit.getOfflinePlayer(targetName).uniqueId

        punishmentService.punishPlayer(targetUUID, PunishmentType.BAN, duration, reason, player.name)

        val timeStr = FormatUtils.formatDuration(duration)
        if (targetPlayer != null && targetPlayer.isOnline) {
            targetPlayer.kickPlayer(FormatUtils.parse("&fYou have been banned by &6${player.name} &ffor (&6$timeStr&f).&7Reason: $reason"))
        }

        MessageUtils.sendSuccess(player, "&fSuccessfully banned &6$targetName &ffor (&6$timeStr&f) &fReason: &7$reason")

        return true
    }
}