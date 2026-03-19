package me.roustytousty.elytrapvp.services.upgrade

import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.ItemUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class UpgradeService(
    private val playerService: PlayerService,
    private val plugin: JavaPlugin
) {

    fun tryUpgradeItem(player: Player, type: UpgradeType) {

        val playerData = playerService.getOrCreatePlayerData(player)

        val currentLevel = type.getLevel(playerData)
        val nextLevel = currentLevel + 1

        val section = UpgradeConfig.getConfig().getConfigurationSection("upgrades.${type.configKey}.$nextLevel")

        if (section == null) {
            MessageUtils.sendError(player, "&fThis item is already &c&lMAXED &fand cannot be upgraded further.")
            return
        }

        val cost = section.getInt("cost")

        if (playerData.gold < cost) {
            MessageUtils.sendError(player, "&fNot enough gold! You need &6&l${cost}g &fto upgrade!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return
        }

        playerData.gold -= cost

        type.setLevel(playerData, nextLevel)

        MessageUtils.sendSuccess(player, "&fUpgrade &a&lsuccessful&f! &6&l${type.displayName} &fis now upgraded!")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
    }

    fun getItem(playerData: PlayerData, type: UpgradeType): ItemStack? {
        val level = type.getLevel(playerData)

        val section = UpgradeConfig.getConfig()
            .getConfigurationSection("upgrades.${type.configKey}.$level")
            ?: return null

        return ItemUtils.kitItemBuilder(section, type, plugin)
    }

    fun getNextUpgradeData(playerData: PlayerData, type: UpgradeType): UpgradeData {
        val level = type.getLevel(playerData) + 1
        return getUpgradeData(type, level)
    }

    fun getCurrentUpgradeData(playerData: PlayerData, type: UpgradeType): UpgradeData {
        val level = type.getLevel(playerData)
        return getUpgradeData(type, level)
    }

    private fun getUpgradeData(type: UpgradeType, level: Int): UpgradeData {

        val section = UpgradeConfig.getConfig()
            .getConfigurationSection("upgrades.${type.configKey}.$level")

        if (section == null) {
            return UpgradeData(
                item = ItemStack(type.material),
                cost = -1,
                level = level,
                maxed = true
            )
        }

        val item = ItemUtils.kitItemBuilder(section, type, plugin)
        val cost = section.getInt("cost", -1)

        return UpgradeData(
            item = item,
            cost = cost,
            level = level,
            maxed = false
        )
    }
}