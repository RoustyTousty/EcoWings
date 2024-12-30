package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
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

            val upgradeConfig = UpgradeConfig.getConfig()

            val gold = CacheConfig.getplrVal(statPlayer, "gold")
            val kills = CacheConfig.getplrVal(statPlayer, "kills")
            val deaths = CacheConfig.getplrVal(statPlayer, "deaths")

            val goldRank = MongoDB.getPlayerRank(statPlayer, "gold")
            val killsRank = MongoDB.getPlayerRank(statPlayer, "kills")
            val deathsRank = MongoDB.getPlayerRank(statPlayer, "deaths")

            val helmetLevel = CacheConfig.getplrVal(statPlayer, "helmetLevel") as? Int ?: 0
            val elytraLevel = CacheConfig.getplrVal(statPlayer, "elytraLevel") as? Int ?: 0
            val leggingsLevel = CacheConfig.getplrVal(statPlayer, "leggingsLevel") as? Int ?: 0
            val bootsLevel = CacheConfig.getplrVal(statPlayer, "bootsLevel") as? Int ?: 0
            val swordLevel = CacheConfig.getplrVal(statPlayer, "swordLevel") as? Int ?: 0
            val shearsLevel = CacheConfig.getplrVal(statPlayer, "shearsLevel") as? Int ?: 0

            val helmetItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.helmet.$helmetLevel")!!)
            val elytraItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.elytra.$elytraLevel")!!)
            val leggingsItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.leggings.$leggingsLevel")!!)
            val bootsItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.boots.$bootsLevel")!!)
            val swordItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.sword.$swordLevel")!!)
            val shearsItem = ItemUtils.kitItemBuilder(upgradeConfig.getConfigurationSection("upgrades.shears.$shearsLevel")!!)

            inventory.setItem(
                11,
                helmetItem
            )
            inventory.setItem(
                20,
                elytraItem
            )
            inventory.setItem(
                29,
                leggingsItem
            )
            inventory.setItem(
                38,
                bootsItem
            )
            inventory.setItem(
                21,
                swordItem
            )
            inventory.setItem(
                30,
                shearsItem
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
                    "&eTop Killstreak",
                    "&7Test!"
                )
            )
            inventory.setItem(
                23,
                ItemUtils.itemBuilder(
                    Material.AMETHYST_SHARD,
                    1,
                    false,
                    "&eRebirths",
                    "&7Test!"
                )
            )
            inventory.setItem(
                14,
                ItemUtils.itemBuilder(
                    Material.GOLD_INGOT,
                    1,
                    false,
                    "&eGold &6#$goldRank",
                    "&7Value: ${formatNumber(gold as Int)}"
                )
            )
            inventory.setItem(
                15,
                ItemUtils.itemBuilder(
                    Material.WOODEN_SWORD,
                    1,
                    false,
                    "&eKills &6#$killsRank",
                    "&7Value: ${formatNumber(kills as Int)}"
                )
            )
            inventory.setItem(
                33,
                ItemUtils.itemBuilder(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths &6#$deathsRank",
                    "&7Value: ${formatNumber(deaths as Int)}"
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