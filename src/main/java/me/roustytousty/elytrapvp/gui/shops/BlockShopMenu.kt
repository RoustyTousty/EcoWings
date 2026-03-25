package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.services.Services
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

class BlockShopMenu : Listener {

    private val shopService = Services.shopService
    private val SHOP_TITLE = "Blocks"

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.view.title != SHOP_TITLE) return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player
        if (e.rawSlot == 27) {
            ShopMenu.openInventory(p)
            return
        }

        val shopItem = shopService.getItemBySlot(SHOP_TITLE, e.rawSlot)
        if (shopItem != null) {
            shopService.shopPurchaseItem(p, shopItem)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == SHOP_TITLE) e.isCancelled = true
    }

    companion object {
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 36, "Blocks")
            initItems(inventory)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 35)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(27, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))

            val items = Services.shopService.getItems("Blocks")
            for (item in items) {

                val lore = mutableListOf<String>()
                lore.addAll(item.description)
                lore.add("")
                lore.add("&fCost: &6${item.cost}g")
                lore.add("")
                lore.add("&7Click to buy!")

                inventory.setItem(
                    item.slot,
                    itemBuilder(
                        item.material,
                        1,
                        false,
                        "&e${item.displayName} &6x${item.amount}",
                        *lore.toTypedArray()
                    )
                )
            }
        }
    }
}