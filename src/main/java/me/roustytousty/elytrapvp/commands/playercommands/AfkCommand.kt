package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AfkCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true

        Services.afkService.toggleAfk(sender)
        return true
    }
}