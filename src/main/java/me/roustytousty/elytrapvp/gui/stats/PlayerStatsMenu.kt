package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.ItemUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class PlayerStatsMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Player stats") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            45 -> StatsMenu.openInventory(p)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Player stats") {
            e.isCancelled = true
        }
    }

    companion object {

        private val leaderboardService = Services.leaderboardService
        private val upgradeService = Services.upgradeService

        fun openInventory(player: Player, statPlayer: Player) {
            val inventory = Bukkit.createInventory(null, 54, "Player stats")
            initItems(inventory, statPlayer)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, statPlayer: Player) {
            val slots = intArrayOf(8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inventory.setItem(slot,
                    ItemUtils.itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val playerData = Services.playerService.getOrCreatePlayerData(statPlayer)

            val gold = playerData.gold
            val rebirths = playerData.rebirths
            val kills = playerData.kills
            val recordKillstreak = playerData.recordKillstreak
            val deaths = playerData.deaths

            val goldRank = leaderboardService.getRankDisplay("gold", statPlayer)
            val rebirthsRank = leaderboardService.getRankDisplay("rebirths", statPlayer)
            val killsRank = leaderboardService.getRankDisplay("kills", statPlayer)
            val recordKillstreakRank = leaderboardService.getRankDisplay("recordKillstreak", statPlayer)
            val deathsRank = leaderboardService.getRankDisplay("deaths", statPlayer)

            for (type in UpgradeType.values()) {
                inventory.setItem(
                    type.statsSlot,
                    getDisplayItem(playerData, type)
                )
            }

            inventory.setItem(0, ItemUtils.itemBuilder(statPlayer, 1, false, "&f"))

            inventory.setItem(24, ItemUtils.itemBuilder(Material.GOLDEN_SWORD, 1, false, "&eRecord killstreak &6$recordKillstreakRank", "&7Value: ${formatNumber(recordKillstreak)}"))
            inventory.setItem(23, ItemUtils.itemBuilder(Material.CHERRY_SAPLING, 1, false, "&eRebirths &6$rebirthsRank", "&7Value: ${formatNumber(rebirths)}"))
            inventory.setItem(14, ItemUtils.itemBuilder(Material.GOLD_INGOT, 1, false, "&eGold &6$goldRank", "&7Value: ${formatNumber(gold)}"))
            inventory.setItem(15, ItemUtils.itemBuilder(Material.WOODEN_SWORD, 1, false, "&eKills &6$killsRank", "&7Value: ${formatNumber(kills)}"))
            inventory.setItem(33, ItemUtils.itemBuilder(Material.SKELETON_SKULL, 1, false, "&eDeaths &6$deathsRank", "&7Value: ${formatNumber(deaths)}"))

            inventory.setItem(45, ItemUtils.itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))
        }

        private fun getDisplayItem(playerData: PlayerData, type: UpgradeType): ItemStack {
            val data = upgradeService.getCurrentUpgradeData(playerData, type)

            if (data.item.type == Material.AIR) {
                return ItemUtils.itemBuilder(
                    Material.ORANGE_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&f${type.displayName} &6[&fT0&6]",
                )
            }

            return ItemUtils.itemBuilder(data.item)
        }
    }
}