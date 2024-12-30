package me.roustytousty.elytrapvp.gui.perks

import me.roustytousty.elytrapvp.gui.upgrade.ConfirmUpgradeMenu
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

class PerksMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Perks") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            45 -> {
                p.closeInventory()
                p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Perks") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 54, "Perks")
            initItems(inventory)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }


            inventory.setItem(
                13,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&eActive perk",
                    "&7Test!"
                )
            )


            inventory.setItem(
                29,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-1",
                    "&7Test!"
                )
            )
            inventory.setItem(
                30,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                31,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                32,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                33,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                38,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                39,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                40,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                41,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )
            inventory.setItem(
                42,
                itemBuilder(
                    Material.BARRIER,
                    1,
                    false,
                    "&ePerk-2",
                    "&7Test!"
                )
            )

            inventory.setItem(
                45,
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