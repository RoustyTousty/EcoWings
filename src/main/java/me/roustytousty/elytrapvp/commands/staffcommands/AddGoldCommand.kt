package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddGoldCommand : CommandExecutor {

    private val playerService = Services.playerService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (args.size != 2) {
            MessageUtils.sendError(player, "&6/adgold &f<player> <number>")
            return false
        }

        val selectedPlayerName = args[0]
        val goldAmount = args[1].toIntOrNull()

        if (goldAmount == null) {
            MessageUtils.sendError(player, "&fInvalid gold amount provided!")
            return false
        }

        val selectedPlayer = Bukkit.getPlayer(selectedPlayerName)
        if (selectedPlayer != null) {
            val selectedPlayerData = playerService.getOrCreatePlayerData(selectedPlayer)
            selectedPlayerData.gold += goldAmount
            MessageUtils.sendMessage(player, "&fSuccessfully added &6$goldAmount &fgold to &6${selectedPlayer.name}&f's gold")
        } else {
            MessageUtils.sendError(player, "&fYou can't add gold to an offline player.")
        }

        return true
    }
}