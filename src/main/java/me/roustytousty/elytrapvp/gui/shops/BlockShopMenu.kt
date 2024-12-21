package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.services.ShopService
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
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

    private val shopService = ShopService()

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11 -> shopService.shopPurchaseItem(p, 1, clickedItem.type, 16)
            12 -> shopService.shopPurchaseItem(p, 3, clickedItem.type, 16)
            13 -> shopService.shopPurchaseItem(p, 10, clickedItem.type, 16)
            14 -> shopService.shopPurchaseItem(p, 6, clickedItem.type, 16)
            15 -> shopService.shopPurchaseItem(p, 12, clickedItem.type, 16)

            18 -> ShopMenu.openInventory(p)
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
                inv!!.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inv!!.setItem(
                11,
                itemBuilder(
                    Material.WHITE_WOOL,
                    1,
                    false,
                    "&eWhite wool &6x16",
                    "&7Just the usual!",
                    "",
                    "&fPrice: &61g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                12,
                itemBuilder(
                    Material.YELLOW_WOOL,
                    1,
                    false,
                    "&eYellow wool &6x16",
                    "&7Has to be broken 2x times!",
                    "",
                    "&fPrice: &63g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                13,
                itemBuilder(
                    Material.ORANGE_WOOL,
                    1,
                    false,
                    "&eOrange wool &6x16",
                    "&7Strongest type of wool!",
                    "",
                    "&fPrice: &610g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                14,
                itemBuilder(
                    Material.WHITE_CONCRETE,
                    1,
                    false,
                    "&eWhite concrete &6x16",
                    "&7Lasts for 30 seconds!",
                    "",
                    "&fPrice: &66g",
                    "",
                    "&7Click to buy!"
                )
            )
            inv!!.setItem(
                15,
                itemBuilder(
                    Material.YELLOW_CONCRETE,
                    1,
                    false,
                    "&eYellow concrete &6x16",
                    "&7Lasts for 1 minute!",
                    "",
                    "&fPrice: &612g",
                    "",
                    "&7Click to buy!"
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