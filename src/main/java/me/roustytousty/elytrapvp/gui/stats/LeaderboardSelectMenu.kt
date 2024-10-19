package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
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

        if (e.rawSlot == 11) {
            LeaderboardMenu.openInventory(p, "kills")
        } else if (e.rawSlot == 13) {
            LeaderboardMenu.openInventory(p, "gold")
        } else if (e.rawSlot == 15) {
            LeaderboardMenu.openInventory(p, "deaths")
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
            inv = Bukkit.createInventory(null, 36, "Leaderboards")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35)
            for (slot in slots) {
                inv!!.setItem(slot, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inv!!.setItem(
                11,
                createGuiItem(
                    Material.IRON_SWORD,
                    1,
                    false,
                    "&eKills",
                    "&7Top players for kills!"
                )
            )
            inv!!.setItem(
                13,
                createGuiItem(
                    Material.GOLD_BLOCK,
                    1,
                    false,
                    "&eGold",
                    "&7Top players for gold!"
                )
            )
            inv!!.setItem(
                15,
                createGuiItem(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths",
                    "&7Top players for deaths!"
                )
            )
        }
    }
}