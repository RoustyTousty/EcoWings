package me.roustytousty.elytrapvp.services.afk

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.currency.CurrencyService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AfkService(
    private val currencyService: CurrencyService
) {
    private val afkPlayers = mutableSetOf<UUID>()

    init {
        startAfkTask()
    }

    fun toggleAfk(player: Player) {
        if (afkPlayers.contains(player.uniqueId)) {
            stopAfk(player)
        } else {
            startAfk(player)
        }
    }

    private fun startAfk(player: Player) {
        afkPlayers.add(player.uniqueId)
        MessageUtils.sendMessage(player, "&fYou are now &6AFK&f. Moving will cancel rewards.")
    }

    fun stopAfk(player: Player) {
        if (afkPlayers.remove(player.uniqueId)) {
            MessageUtils.sendMessage(player, "&fYou are no longer &6AFK&f.")
        }
    }

    fun isAfk(player: Player): Boolean = afkPlayers.contains(player.uniqueId)

    private fun startAfkTask() {
        object : BukkitRunnable() {
            override fun run() {
                for (uuid in afkPlayers) {
                    val player = Bukkit.getPlayer(uuid) ?: continue
                    rollRewards(player)
                }
            }
        }.runTaskTimer(ElytraPVP.instance!!, 1200L, 1200L)
    }

    private fun rollRewards(player: Player) {
        val random = ThreadLocalRandom.current().nextDouble(100.0)

        when {
            random <= 0.1 -> {
                currencyService.giveShards(player, 1, "AFK Reward")
                MessageUtils.sendMessage(player, "&6&lRARE&f! &fYou found &61 &fShard while &6AFK&f!")
            }
            random <= 5.1 -> {
                currencyService.giveGold(player, 50, "AFK Reward")
            }
            random <= 25.1 -> {
                currencyService.giveGold(player, 10, "AFK Reward")
            }
        }
    }
}