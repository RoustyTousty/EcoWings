package me.roustytousty.elytrapvp.gui.shops

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

class ShopMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11 -> BlockShopMenu.openInventory(p)
            12 -> UtilityShopMenu.openInventory(p)
            13 -> ConsumablesShopMenu.openInventory(p)

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
            inv = Bukkit.createInventory(null, 27, "Shop")
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
                    "&eBlocks",
                    "&7Buy different types of blocks!"
                )
            )
            inv!!.setItem(
                12,
                itemBuilder(
                    Material.FEATHER,
                    1,
                    false,
                    "&eUtility",
                    "&7Usable utility during pvp!"
                )
            )
            inv!!.setItem(
                13,
                itemBuilder(
                    Material.GLASS_BOTTLE,
                    1,
                    false,
                    "&eConsumables",
                    "&7Consumables provide small boosts!"
                )
            )
            inv!!.setItem(
                14,
                itemBuilder(
                    Material.GUNPOWDER,
                    1,
                    false,
                    "&eRockets",
                    "&7Need to go higher?"
                )
            )
            inv!!.setItem(
                15,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&cComing soon",
                    "&7I didn't know what to put here :)"
                )
            )


            inv!!.setItem(
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