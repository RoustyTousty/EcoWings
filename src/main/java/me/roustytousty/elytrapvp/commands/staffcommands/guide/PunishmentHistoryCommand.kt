package me.roustytousty.elytrapvp.commands.staffcommands.guide

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.text.SimpleDateFormat

class PunishmentHistoryCommand : CommandExecutor {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!player.hasPermission("group.guide")) {
            MessageUtils.sendError(player, "&fYou do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtils.sendError(player, "&fUsage: /punishmenthistory <player>")
            return true
        }

        val targetName = args[0]
        val targetUUID = Bukkit.getPlayerExact(targetName)?.uniqueId ?: Bukkit.getOfflinePlayer(targetName).uniqueId
        val data = Services.playerService.getPlayerDataByUUID(targetUUID)

        if (data == null) {
            MessageUtils.sendError(player, "&cPlayer data not found.")
            return true
        }

        MessageUtils.sendMessage(player, "&fPunishment History for §6$targetName")
        MessageUtils.sendMessage(player, "&7----------------------------------")

        if (data.punishments.isEmpty()) {
            MessageUtils.sendError(player, "&fNo punishments found.")
        } else {
            data.punishments.sortedByDescending { it.timestamp }.forEach { pun ->
                val status = if (pun.isExpired()) "&f[&aExpired&f]" else "&f[&cActive&f]"

                MessageUtils.sendMessage(player, " ")
                MessageUtils.sendMessage(player, "&fType: &6${pun.type} $status")
                MessageUtils.sendMessage(player, "&fDuration: &6${FormatUtils.formatDuration(pun.durationMillis)}")
                MessageUtils.sendMessage(player, "&fPunisher: &6${pun.issuer}")
                MessageUtils.sendMessage(player, "&fReason: &7${pun.reason}")
                MessageUtils.sendMessage(player, " ")
            }
        }
        MessageUtils.sendMessage(player, "&7----------------------------------")
        return true
    }
}