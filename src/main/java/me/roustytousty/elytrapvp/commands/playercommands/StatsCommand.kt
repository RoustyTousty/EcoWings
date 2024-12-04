package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.gui.stats.PlayerStatsMenu
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (args.isEmpty()) {
            PlayerStatsMenu.openInventory(player, player)
            return true
        }

        val targetPlayerName = args[0]
        val targetPlayer = Bukkit.getPlayer(targetPlayerName)

        if (targetPlayer == null || !targetPlayer.isOnline) {
            player.sendMessage(StringUtils.parse("&c&lEcoWings &8| &fPlayer must be &c&lONLINE &fto see there stats!"))
            return true
        }

        PlayerStatsMenu.openInventory(player, targetPlayer)
        player.sendMessage(StringUtils.parse("&a&lEcoWings &8| &fOpening stats for &6${targetPlayer.name}&f!"))

        return true
    }
}