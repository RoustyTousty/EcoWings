package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import kotlin.math.max

class CategoryShopMenu : Listener {

    private val shopService = Services.shopService

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val title = e.view.title
        val items = shopService.getShopItems(title)

        if (items.isEmpty()) return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        val backSlot = e.inventory.size - 9
        if (e.rawSlot == backSlot) {
            ShopMenu.openInventory(p)
            return
        }

        val shopItem = shopService.getShopItemBySlot(title, e.rawSlot)
        if (shopItem != null) {
            shopService.tryPlayerPurchaseItem(p, shopItem)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (shopService.getShopItems(e.view.title).isNotEmpty()) {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player, shopType: String) {
            val items = Services.shopService.getShopItems(shopType)
            if (items.isEmpty()) return

            val maxSlot = items.maxOfOrNull { it.slot } ?: 0

            var size = ((maxSlot / 9) + 2) * 9
            size = max(27, size)

            val inventory = Bukkit.createInventory(null, size, shopType)
            initItems(inventory, shopType)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, shopType: String) {
            val size = inventory.size

            for (i in 0 until size step 9) {
                inventory.setItem(i, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")) // Left edge
                inventory.setItem(i + 8, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")) // Right edge
            }

            val backSlot = size - 9
            inventory.setItem(backSlot, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))

            val items = Services.shopService.getShopItems(shopType)
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