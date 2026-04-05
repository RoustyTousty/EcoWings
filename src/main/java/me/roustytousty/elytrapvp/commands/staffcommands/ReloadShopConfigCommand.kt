package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.configs.ShopConfig
import me.roustytousty.elytrapvp.services.Services
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadShopConfigCommand : CommandExecutor {

    private val shopService = Services.shopService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        ShopConfig.load()

        shopService.loadItems()

        return true
    }
}