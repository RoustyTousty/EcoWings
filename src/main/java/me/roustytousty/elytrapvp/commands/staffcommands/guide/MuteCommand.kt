package me.roustytousty.elytrapvp.commands.staffcommands.guide

import me.roustytousty.elytrapvp.data.model.PunishmentType
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
        if (!player.hasPermission("group.guide")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: &6/mute <player> [time] [reason]")
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
            "Spam/Toxicity"
        }

        val targetPlayer = Bukkit.getPlayerExact(targetName)
        val targetUUID = targetPlayer?.uniqueId ?: Bukkit.getOfflinePlayer(targetName).uniqueId

        punishmentService.punishPlayer(targetUUID, PunishmentType.MUTE, duration, reason, player.name)

        val timeStr = FormatUtils.formatDuration(duration)
        MessageUtils.sendSuccess(player, "&fMuted &6$targetName &ffor (&6$timeStr&f). &7Reason: $reason")

        if (targetPlayer != null && targetPlayer.isOnline) {
            MessageUtils.sendError(targetPlayer, "&fYou have been muted by &6${player.name} &ffor (&6$timeStr&f). &fReason: &7$reason")
        }

        return true
    }
}