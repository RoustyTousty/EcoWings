package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuildModeCommand : CommandExecutor {

    private val playerService = Services.playerService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val playerData = playerService.getOrCreatePlayerData(player)

        val buildmode = playerData.isBuildMode
        if (buildmode) {
            playerData.isBuildMode = false
            MessageUtils.sendMessage(player, "&6Buildmode &fis now &c&lOFF&f!")
        } else {
            playerData.isBuildMode = true
            MessageUtils.sendMessage(player, "&6Buildmode &fis now &a&lON&f!")
        }

        return true
    }
}