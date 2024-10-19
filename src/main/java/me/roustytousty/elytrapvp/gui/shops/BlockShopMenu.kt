package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
import me.roustytousty.elytrapvp.utility.MiscUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class BlockShopMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 11) {
            MiscUtils.shopPurchaseItem(p, 1, clickedItem.type, 16)
        } else if (e.rawSlot == 12) {
            MiscUtils.shopPurchaseItem(p, 3, clickedItem.type, 16)
        } else if (e.rawSlot == 13) {
            MiscUtils.shopPurchaseItem(p, 10, clickedItem.type, 16)
        } else if (e.rawSlot == 14) {
            MiscUtils.shopPurchaseItem(p, 6, clickedItem.type, 16)
        } else if (e.rawSlot == 15) {
            MiscUtils.shopPurchaseItem(p, 12, clickedItem.type, 16)
        } else if (e.rawSlot == 18) {
            ShopMenu.openInventory(p)
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
            inv = Bukkit.createInventory(null, 27, "Blocks")
            initItems()
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems() {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26)
            for (slot in slots) {
                inv!!.setItem(slot, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inv!!.setItem(
                11,
                createGuiItem(
                    Material.WHITE_WOOL,
                    1,
                    false,
                    "&fWhite wool &6[&f16x&6]",
                    "&7Just the usual!",
                    "",
                    "&fCost: &61g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                12,
                createGuiItem(
                    Material.YELLOW_WOOL,
                    1,
                    false,
                    "&fLight gray wool &6[&f16x&6]",
                    "&7Has to be broken 2x times!",
                    "",
                    "&fCost: &63g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                13,
                createGuiItem(
                    Material.ORANGE_WOOL,
                    1,
                    false,
                    "&fGray wool &6[&f16x&6]",
                    "&7Strongest type of wool!",
                    "",
                    "&fCost: &610g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                14,
                createGuiItem(
                    Material.WHITE_CONCRETE,
                    1,
                    false,
                    "&fWhite concrete &6[&f16x&6]",
                    "&7Lasts for 30 seconds!",
                    "",
                    "&fCost: &66g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                15,
                createGuiItem(
                    Material.LIGHT_GRAY_CONCRETE,
                    1,
                    false,
                    "&fLight gray concrete &6[&f16x&6]",
                    "&7Lasts for 1 minute!",
                    "",
                    "&fCost: &612g",
                    "",
                    "&7Click to buy!"
                )
            )


            inv!!.setItem(
                18,
                createGuiItem(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cBack"
                )
            )
        }
    }
}