package me.roustytousty.elytrapvp.services.shop

import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Sound
import org.bukkit.entity.Player

class UpgradeService(
    private val playerService: PlayerService
) {

    fun tryUpgradeItem(player: Player, type: UpgradeType) {

        val playerData = playerService.getOrCreatePlayerData(player)

        val currentLevel = type.getLevel(playerData)
        val nextLevel = currentLevel + 1

        val section = UpgradeConfig.getConfig().getConfigurationSection("upgrades.${type.configKey}.$nextLevel")

        if (section == null) {
            MessageUtils.sendError(player, "&cThis item is already maxed!")
            return
        }

        val cost = section.getInt("cost")

        if (playerData.gold < cost) {
            MessageUtils.sendError(player, "&fNot enough gold! You need &6${cost}g")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return
        }

        playerData.gold -= cost

        type.setLevel(playerData, nextLevel)

        MessageUtils.sendSuccess(player, "&fUpgrade successful! ${type.displayName} &fis now level &6$nextLevel")

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
    }
}