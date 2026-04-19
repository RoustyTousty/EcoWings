package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RulesCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command.")
            return true
        }

        val player = sender

        MessageUtils.sendMessage(player, "&6&lServer Rules:")
        MessageUtils.sendMessage(player, "&e1. &fNo toxicity.")
        MessageUtils.sendMessage(player, "&e2. &fNo hacking, exploiting or cheating.")
        MessageUtils.sendMessage(player, "&e3. &fNo spamming.")
        MessageUtils.sendMessage(player, "&e4. &fNo racism.")
        MessageUtils.sendMessage(player, "&e5. &fDon't swear or bypass the Minehut swearing filter.")
        MessageUtils.sendMessage(player, "&e6. &fDon't break the Minehut TOS rules.")
        MessageUtils.sendMessage(player, "&e7. &fUse common sense.")
        MessageUtils.sendMessage(player, "")
        MessageUtils.sendMessage(player, "&a&lAnd have fun on the server!")

        return true
    }
}