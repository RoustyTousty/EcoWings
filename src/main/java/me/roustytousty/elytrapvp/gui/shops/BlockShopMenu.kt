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

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Blocks") return

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
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Blocks") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Blocks")
            initItems(inventory)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(
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
            inventory.setItem(
                12,
                itemBuilder(
                    Material.LIGHT_GRAY_WOOL,
                    1,
                    false,
                    "&eLight gray wool &6x16",
                    "&7Can't turn to dust!",
                    "",
                    "&fPrice: &63g",
                    "",
                    "&7Click to buy!"
                )
            )
            inventory.setItem(
                13,
                itemBuilder(
                    Material.OAK_PLANKS,
                    1,
                    false,
                    "&eOak planks &6x16",
                    "&7Simple wood planks!",
                    "",
                    "&fPrice: &610g",
                    "",
                    "&7Click to buy!"
                )
            )
            inventory.setItem(
                14,
                itemBuilder(
                    Material.STONE_BRICKS,
                    1,
                    false,
                    "&eStone bricks &6x16",
                    "&7Simple stone bricks!",
                    "",
                    "&fPrice: &66g",
                    "",
                    "&7Click to buy!"
                )
            )
            inventory.setItem(
                15,
                itemBuilder(
                    Material.DEEPSLATE_BRICKS,
                    1,
                    false,
                    "&eStronger stone bricks &6x16",
                    "&7Lasts for 1 minute!",
                    "",
                    "&fPrice: &612g",
                    "",
                    "&7Click to buy!"
                )
            )


            inventory.setItem(
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