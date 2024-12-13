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
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 45) {
            StatsMenu.openInventory(p)
        }
    }

    @EventHandler
    private fun onInventoryClick(e: InventoryDragEvent) {
        if (e.inventory == inv) {
            e.isCancelled = true
        }
    }

    companion object {

        var inv: Inventory? = null
        fun openInventory(player: Player, statPlayer: Player) {
            inv = Bukkit.createInventory(null, 54, "Stats - ${statPlayer.name}")
            initItems(statPlayer)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(statPlayer: Player) {
            val slots = intArrayOf(8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val cacheConfig = UpgradeConfig.getConfig()

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

            val helmetItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.helmet.$helmetLevel")!!)
            val elytraItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.elytra.$elytraLevel")!!)
            val leggingsItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.leggings.$leggingsLevel")!!)
            val bootsItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.boots.$bootsLevel")!!)
            val swordItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.sword.$swordLevel")!!)
            val shearsItem = ItemUtils.kitItemBuilder(cacheConfig.getConfigurationSection("upgrades.shears.$shearsLevel")!!)

            inv!!.setItem(
                11,
                helmetItem
            )
            inv!!.setItem(
                20,
                elytraItem
            )
            inv!!.setItem(
                29,
                leggingsItem
            )
            inv!!.setItem(
                38,
                bootsItem
            )
            inv!!.setItem(
                21,
                swordItem
            )
            inv!!.setItem(
                30,
                shearsItem
            )

            inv!!.setItem(
                0,
                ItemUtils.itemBuilder(
                    statPlayer,
                    1,
                    false,
                    "&f"
                )
            )
            inv!!.setItem(
                24,
                ItemUtils.itemBuilder(
                    Material.GOLDEN_SWORD,
                    1,
                    false,
                    "&eTop Killstreak",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                23,
                ItemUtils.itemBuilder(
                    Material.AMETHYST_SHARD,
                    1,
                    false,
                    "&eRebirths",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                14,
                ItemUtils.itemBuilder(
                    Material.GOLD_INGOT,
                    1,
                    false,
                    "&eGold &6#$goldRank",
                    "&7Value: ${formatNumber(gold as Int)}"
                )
            )
            inv!!.setItem(
                15,
                ItemUtils.itemBuilder(
                    Material.WOODEN_SWORD,
                    1,
                    false,
                    "&eKills &6#$killsRank",
                    "&7Value: ${formatNumber(kills as Int)}"
                )
            )
            inv!!.setItem(
                33,
                ItemUtils.itemBuilder(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths &6#$deathsRank",
                    "&7Value: ${formatNumber(deaths as Int)}"
                )
            )

            inv!!.setItem(
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