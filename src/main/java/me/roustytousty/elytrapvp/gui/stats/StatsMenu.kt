package me.roustytousty.elytrapvp.gui.stats

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

class StatsMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            12 -> PlayerStatsMenu.openInventory(p, p)
            14 -> LeaderboardSelectMenu.openInventory(p)

            18 -> {
                p.closeInventory()
                p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
            }
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
            inv = Bukkit.createInventory(null, 27, "Stats")
            initItems(player)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }


            inv!!.setItem(
                12,
                ItemUtils.itemBuilder(
                    player,
                    1,
                    false,
                    "&eStats",
                    "&7Your personal stats menu!"
                )
            )
            inv!!.setItem(
                14,
                ItemUtils.itemBuilder(
                    Material.GOLD_BLOCK,
                    1,
                    false,
                    "&eLeaderboards",
                    "&7Top players in different stats!"
                )
            )

            inv!!.setItem(
                18,
                ItemUtils.itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}