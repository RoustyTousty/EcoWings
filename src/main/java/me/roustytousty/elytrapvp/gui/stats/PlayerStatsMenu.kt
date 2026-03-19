package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.ItemUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

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
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
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

            val goldRank = leaderboardService.getRank("gold", statPlayer)
            val rebirthsRank = leaderboardService.getRank("rebirths", statPlayer)
            val killsRank = leaderboardService.getRank("kills", statPlayer)
            val recordKillstreakRank = leaderboardService.getRank("recordKillstreak", statPlayer)
            val deathsRank = leaderboardService.getRank("deaths", statPlayer)

            val helmetData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.HELMET)
            val elytraData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.ELYTRA)
            val leggingsData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.LEGGINGS)
            val bootsData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.BOOTS)
            val swordData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.SWORD)
            val shearsData = upgradeService.getCurrentUpgradeData(playerData, UpgradeType.SHEARS)

            inventory.setItem(
                11,
                helmetData.item
            )
            inventory.setItem(
                20,
                elytraData.item
            )
            inventory.setItem(
                29,
                leggingsData.item
            )
            inventory.setItem(
                38,
                bootsData.item
            )
            inventory.setItem(
                21,
                swordData.item
            )
            inventory.setItem(
                30,
                shearsData.item
            )

            inventory.setItem(
                0,
                ItemUtils.itemBuilder(
                    statPlayer,
                    1,
                    false,
                    "&f"
                )
            )
            inventory.setItem(
                24,
                ItemUtils.itemBuilder(
                    Material.GOLDEN_SWORD,
                    1,
                    false,
                    "&eRecord killstreak &6#$recordKillstreakRank",
                    "&7Value: ${formatNumber(recordKillstreak)}"
                )
            )
            inventory.setItem(
                23,
                ItemUtils.itemBuilder(
                    Material.CHERRY_SAPLING,
                    1,
                    false,
                    "&eRebirths &6#$rebirthsRank",
                    "&7Value: ${formatNumber(rebirths)}"
                )
            )
            inventory.setItem(
                14,
                ItemUtils.itemBuilder(
                    Material.GOLD_INGOT,
                    1,
                    false,
                    "&eGold &6#$goldRank",
                    "&7Value: ${formatNumber(gold)}"
                )
            )
            inventory.setItem(
                15,
                ItemUtils.itemBuilder(
                    Material.WOODEN_SWORD,
                    1,
                    false,
                    "&eKills &6#$killsRank",
                    "&7Value: ${formatNumber(kills)}"
                )
            )
            inventory.setItem(
                33,
                ItemUtils.itemBuilder(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths &6#$deathsRank",
                    "&7Value: ${formatNumber(deaths)}"
                )
            )

            inventory.setItem(
                45,
                ItemUtils.itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cBack"
                )
            )
        }
    }
}