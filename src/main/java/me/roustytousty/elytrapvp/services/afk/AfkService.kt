package me.roustytousty.elytrapvp.services.afk

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.currency.CurrencyService
import me.roustytousty.elytrapvp.services.region.RegionService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AfkService(
    private val currencyService: CurrencyService,
    private val regionService: RegionService
) {
    private val manualAfkPlayers = mutableSetOf<UUID>()
    val afkRegionName = "afkRegion"
    val spawnRegionName = "spawnRegion"

    init {
        startAfkTask()
    }

    fun toggleAfk(player: Player) {
        if (!isInSpawn(player)) {
            MessageUtils.sendMessage(player, "&cYou must be in the spawn region to use /afk!")
            return
        }

        if (manualAfkPlayers.contains(player.uniqueId)) {
            stopAfk(player)
        } else {
            startAfk(player)
        }
    }

    fun startAfk(player: Player) {
        if (manualAfkPlayers.contains(player.uniqueId)) return
        manualAfkPlayers.add(player.uniqueId)
        MessageUtils.sendMessage(player, "&fYou are now &6AFK&f. Moving will cancel rewards.")
    }

    fun stopAfk(player: Player) {
        if (manualAfkPlayers.remove(player.uniqueId)) {
            MessageUtils.sendMessage(player, "&fYou are no longer &6AFK&f.")
        }
    }

    fun isManuallyAfk(player: Player): Boolean = manualAfkPlayers.contains(player.uniqueId)

    fun isInAfkRegion(player: Player): Boolean = regionService.isInRegion(player.location, afkRegionName)

    fun isInSpawn(player: Player): Boolean = regionService.isInRegion(player.location, spawnRegionName)

    private fun startAfkTask() {
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (isManuallyAfk(player) || isInAfkRegion(player)) {
                        rollRewards(player)
                    }
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
            random <= 5.1 -> currencyService.giveGold(player, 50, "AFK Reward")
            random <= 25.1 -> currencyService.giveGold(player, 10, "AFK Reward")
            else -> {
                currencyService.giveGold(player, 1, "AFK Reward")
            }
        }
    }
}