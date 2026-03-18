package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.ItemUtils
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

class ConfirmUpgradeMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {

        val holder = e.inventory.holder

        if (holder !is ConfirmUpgradeHolder) return

        e.isCancelled = true

        val player = e.whoClicked as Player
        val type = holder.type

        when (e.rawSlot) {

            0 -> {
                UpgradeMenu.openInventory(player)
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
            }

            8 -> {
                Services.upgradeService.tryUpgradeItem(player, type)
                Services.kitService.syncKit(player)
                player.closeInventory()
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.inventory.holder is ConfirmUpgradeHolder) {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player, type: UpgradeType) {

            val holder = ConfirmUpgradeHolder(type)

            val inventory: Inventory = Bukkit.createInventory(holder, 9, "Confirm upgrade")

            holder.setInventory(inventory)

            initItems(inventory, player, type)

            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }

        private fun initItems(
            inventory: Inventory,
            player: Player,
            type: UpgradeType
        ) {

            val fillerSlots = intArrayOf(1,2,3,5,6,7)

            for (slot in fillerSlots) {
                inventory.setItem(
                    slot,
                    itemBuilder(Material.BLACK_STAINED_GLASS_PANE,1,false,"&f")
                )
            }

            val playerData = Services.playerService.getOrCreatePlayerData(player)

            val itemData = Services.upgradeService.getNextUpgradeData(playerData, type)

            inventory.setItem(4, itemData.item)

            inventory.setItem(
                0,
                itemBuilder(Material.RED_STAINED_GLASS_PANE,1,false,"&cCancel")
            )

            inventory.setItem(
                8,
                itemBuilder(
                    Material.LIME_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&aConfirm",
                    "",
                    "&fUpgrade: &6${itemData.cost}g",
                    "",
                    "&7Click to confirm!"
                )
            )
        }
    }
}