package me.roustytousty.elytrapvp.services.currency

import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.services.player.PlayerService
import org.bukkit.entity.Player

class CurrencyService(
    private val playerService: PlayerService,
) {
    fun giveGold(player: Player, baseAmount: Int, reason: String? = null) {
        val playerData = playerService.getOrCreatePlayerData(player)
        val finalAmount = applyMultipliers(player, baseAmount)

        playerData.gold += finalAmount
        sendFeedback(player, finalAmount, reason)
    }

    private fun applyMultipliers(player: Player, amount: Int): Int {
        val playerData = playerService.getOrCreatePlayerData(player)

        val multiplier = 1.0 + (playerData.rebirths * 0.2)

        return (amount * multiplier).toInt()
    }

    private fun sendFeedback(player: Player, amount: Int, reason: String?) {
        player.sendMessage(FormatUtils.parse("&6+${amount}g"))
    }
}