package me.roustytousty.elytrapvp.services.currency

import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils
import org.bukkit.entity.Player

class CurrencyService(
    private val playerService: PlayerService,
) {


    fun giveRewards(player: Player, rewards: Map<CurrencyType, Int>, reason: String? = null) {
        val playerData = playerService.getOrCreatePlayerData(player)
        val feedbackParts = mutableListOf<String>()

        for ((currency, baseAmount) in rewards) {
            if (baseAmount <= 0) continue

            val finalAmount = if (currency == CurrencyType.GOLD) {
                applyMultipliers(player, baseAmount)
            } else {
                baseAmount
            }

            when (currency) {
                CurrencyType.GOLD -> playerData.gold += finalAmount
                CurrencyType.SHARDS -> playerData.shards += finalAmount
            }

            feedbackParts.add("${currency.colorPrefix}+${finalAmount}${currency.suffix}")
        }

        if (feedbackParts.isNotEmpty()) {
            val message = feedbackParts.joinToString(" ")
            player.sendMessage(FormatUtils.parse(message))
        }
    }

    fun giveGold(player: Player, baseAmount: Int, reason: String? = null) {
        giveRewards(player, mapOf(CurrencyType.GOLD to baseAmount), reason)
    }

    fun giveShards(player: Player, baseAmount: Int, reason: String? = null) {
        giveRewards(player, mapOf(CurrencyType.SHARDS to baseAmount), reason)
    }

    private fun applyMultipliers(player: Player, amount: Int): Int {
        val playerData = playerService.getOrCreatePlayerData(player)

        val multiplier = 1.0 + (playerData.rebirths * 0.2)

        return (amount * multiplier).toInt()
    }
}