package me.roustytousty.elytrapvp.gui.events

import me.roustytousty.elytrapvp.gui.upgrade.ConfirmUpgradeMenu
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

class WarTableMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            40 -> {
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
            inv = Bukkit.createInventory(null, 45, "War Table")
            initItems(player)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(player: Player) {
            val slots = intArrayOf(3, 5, 12, 14, 21, 23, 30, 32, 39, 41)
            for (slot in slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }


            inv!!.setItem(
                4,
                ItemUtils.itemBuilder(
                    Material.BOOK,
                    1,
                    false,
                    "&eInfo",
                )
            )
            inv!!.setItem(
                4,
                ItemUtils.itemBuilder(
                    Material.DIAMOND,
                    1,
                    false,
                    "&eRewards",
                )
            )
            inv!!.setItem(
                31,
                ItemUtils.itemBuilder(
                    Material.LECTERN,
                    1,
                    false,
                    "&eWar status",
                )
            )


            inv!!.setItem(
                1,
                ItemUtils.itemBuilder(
                    Material.REINFORCED_DEEPSLATE,
                    1,
                    false,
                    "&c&lOUTLAW",
                )
            )
            inv!!.setItem(
                7,
                ItemUtils.itemBuilder(
                    Material.PACKED_ICE,
                    1,
                    false,
                    "&3&lMARSHAL",
                )
            )

            inv!!.setItem(
                28,
                ItemUtils.itemBuilder(
                    Material.GOLD_BLOCK,
                    1,
                    false,
                    "&eOutlaw leaderboard",
                    "&7Top players in Outlaw",
                    "&7faction!"
                )
            )
            inv!!.setItem(
                34,
                ItemUtils.itemBuilder(
                    Material.GOLD_BLOCK,
                    1,
                    false,
                    "&eMarshal leaderboard",
                    "&7Top players in Marshal",
                    "&7faction!"
                )
            )

            inv!!.setItem(
                27,
                ItemUtils.itemBuilder(
                    player,
                    1,
                    false,
                    "&eBest Outlaw",
                    "",
                    "&7Name: RoustyTousty",
                    "&7Value: 122 FP",
                )
            )
            inv!!.setItem(
                35,
                ItemUtils.itemBuilder(
                    player,
                    1,
                    false,
                    "&eBest Marshal",
                    "",
                    "&7Name: RoustyTousty",
                    "&7Value: 85 FP",
                )
            )



            inv!!.setItem(
                40,
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