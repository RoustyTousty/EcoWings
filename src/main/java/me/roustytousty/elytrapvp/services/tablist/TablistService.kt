package me.roustytousty.elytrapvp.services.tablist

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.LuckPermsUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TablistService {

    init {
        startUpdateTask()
    }

    fun startUpdateTask() {
        object : BukkitRunnable() {
            override fun run() {
                val onlineCount = Bukkit.getOnlinePlayers().size

                val leftPadding = "                          "
                val rightPadding = "                     "

                val header = parse(
                    "\n$leftPadding&6&lEcoWings$rightPadding\n" +
                            "&7Elytra PvP/FFA Server\n"
                )

                val footer = parse(
                    "\n&fPlayers: &6$onlineCount\n" +
                            "\n" +
                            "&fDiscord: &6/discord\n" +
                            "&fStore: &6/store\n" +
                            "\n" +
                            "&7ecowings.minehut.gg\n"
                )

                for (player in Bukkit.getOnlinePlayers()) {
                    updateForPlayer(player, header, footer)
                }
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0L, 20L * 1)
    }

    private fun updateForPlayer(player: Player, header: String, footer: String) {
        player.setPlayerListHeaderFooter(header, footer)

        val prefix = LuckPermsUtils.getPrefix(player)
        player.setPlayerListName(parse("$prefix ${player.name}"))
    }
}