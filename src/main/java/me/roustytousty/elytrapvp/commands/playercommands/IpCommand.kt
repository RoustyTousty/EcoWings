package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class IpCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        MessageUtils.sendMessage(player, "&fServer ip: &6ecowings.minehut.gg")

        return true
    }
}