package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetKitLevelCommand : CommandExecutor {

    private val playerService = Services.playerService
    private val kitService = Services.kitService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        val player = sender

        if (args.size != 3) {
            MessageUtils.sendError(player, "&6/setkitlevel &f<player> <kit> <level>")
            return false
        }

        val targetName = args[0]
        val kitName = args[1].uppercase()
        val level = args[2].toIntOrNull()

        if (level == null || level < 0) {
            MessageUtils.sendError(player, "&fInvalid level provided!")
            return false
        }

        val targetPlayer = Bukkit.getPlayer(targetName)
        if (targetPlayer == null) {
            MessageUtils.sendError(player, "&fPlayer &6&l$targetName &fis not online!")
            return false
        }

        val targetData = playerService.getOrCreatePlayerData(targetPlayer)

        val upgradeType = try {
            UpgradeType.valueOf(kitName)
        } catch (e: IllegalArgumentException) {
            MessageUtils.sendError(player, "&fInvalid kit name! Use one of: &6&l${UpgradeType.values().joinToString { it.name }}&f!")
            return false
        }

        upgradeType.setLevel(targetData, level)

        kitService.syncKit(targetPlayer)

        MessageUtils.sendMessage(player, "&fSuccessfully set &6&l${targetPlayer.name}&f's &6&l${upgradeType.displayName} &flevel to &6&l$level&f!")
        MessageUtils.sendMessage(targetPlayer, "&fYour ${upgradeType.displayName} &flevel has been set to &6&l$level&f!")
        return true
    }
}