package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class LeaderboardSelectMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11 -> LeaderboardMenu.openInventory(p, "kills")
            13 -> LeaderboardMenu.openInventory(p, "gold")
            15 -> LeaderboardMenu.openInventory(p, "deaths")

            18 -> StatsMenu.openInventory(p)
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
        fun openInventory(player: Player) {
            inv = Bukkit.createInventory(null, 27, "Leaderboards")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inv!!.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inv!!.setItem(
                11,
                itemBuilder(
                    Material.IRON_SWORD,
                    1,
                    false,
                    "&eKills",
                    "&7Top players for kills!"
                )
            )
            inv!!.setItem(
                13,
                itemBuilder(
                    Material.GOLD_BLOCK,
                    1,
                    false,
                    "&eGold",
                    "&7Top players for gold!"
                )
            )
            inv!!.setItem(
                15,
                itemBuilder(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths",
                    "&7Top players for deaths!"
                )
            )

            inv!!.setItem(
                18,
                itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cBack"
                )
            )
        }
    }
}