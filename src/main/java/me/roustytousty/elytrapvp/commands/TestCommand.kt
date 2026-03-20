package me.roustytousty.elytrapvp.commands

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        Services.combatService.tag(player, player)

        player.sendMessage("§aYou are now in combat for 5 seconds!")

        return true
    }
}