package me.roustytousty.elytrapvp.gui.shops

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class ShopMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Shop") return

        e.isCancelled = true
        val clickedItem = e.currentItem ?: return
        if (clickedItem.type.isAir) return

        val player = e.whoClicked as Player
        when (e.rawSlot) {
            11 -> BlockShopMenu.openInventory(player)
            12 -> UtilityShopMenu.openInventory(player)
            13 -> ConsumablesShopMenu.openInventory(player)
            18 -> {
                player.closeInventory()
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Shop") {
            e.isCancelled = true
        }
    }

    companion object {
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Shop")
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
                    "&eBlocks",
                    "&7Buy different types of blocks!"
                )
            )
            inventory.setItem(
                12,
                itemBuilder(
                    Material.FEATHER,
                    1,
                    false,
                    "&eUtility",
                    "&7Usable utility during pvp!"
                )
            )
            inventory.setItem(
                13,
                itemBuilder(
                    Material.GLASS_BOTTLE,
                    1,
                    false,
                    "&eConsumables",
                    "&7Consumables provide small boosts!"
                )
            )
            inventory.setItem(
                14,
                itemBuilder(
                    Material.GUNPOWDER,
                    1,
                    false,
                    "&eRockets",
                    "&7Need to go higher?"
                )
            )
            inventory.setItem(
                15,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&cComing soon",
                    "&7I didn't know what to put here :)"
                )
            )
            inventory.setItem(
                18,
                itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}