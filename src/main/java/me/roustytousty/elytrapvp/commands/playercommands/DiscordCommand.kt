package me.roustytousty.elytrapvp.commands.playercommands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DiscordCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val discordLink = "https://discord.gg/ptumUhNERx"

        val message = Component.text()
            .append(
                Component.text("EcoWings ")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
            )
            .append(
                Component.text("| ")
                    .color(NamedTextColor.DARK_GRAY)
            )
            .append(
                Component.text("CLICK HERE")
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(Component.text("Join our community Discord!").color(NamedTextColor.GREEN)))
                    .clickEvent(ClickEvent.openUrl(discordLink))
            )
            .append(
                Component.text(" to join our community discord server!")
                    .color(NamedTextColor.WHITE)
            )
            .build()

        player.sendMessage(message)

        return true
    }
}