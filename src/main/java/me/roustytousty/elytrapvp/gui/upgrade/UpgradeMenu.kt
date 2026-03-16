package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.shop.UpgradeType
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class UpgradeMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Upgrade") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        val itemMeta = clickedItem.itemMeta ?: return
        val itemName = itemMeta.displayName

        if (itemName.contains("MAXED", ignoreCase = true)) {
            MessageUtils.sendError(p, "&fThis item is already &c&lMAXED &fand cannot be upgraded further.")
            p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        for (type in UpgradeType.values()) {
            if (e.rawSlot == type.slot) {
                ConfirmUpgradeMenu.openInventory(p, type)
                return
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Upgrade") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 36, "Upgrade")
            initItems(inventory, player)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory, player: Player) {

            val playerData = Services.playerService.getOrCreatePlayerData(player)

            for (type in UpgradeType.values()) {

                val currentLevel = type.getLevel(playerData)
                val nextLevel = currentLevel + 1

                val cost = UpgradeConfig.getConfig().getInt("upgrades.${type.configKey}.$nextLevel.cost", -1)

                if (cost == -1) {

                    inventory.setItem(
                        type.slot,
                        itemBuilder(
                            type.material,
                            1,
                            false,
                            "${type.displayName} &c&lMAXED"
                        )
                    )

                } else {

                    inventory.setItem(
                        type.slot,
                        itemBuilder(
                            type.material,
                            1,
                            false,
                            type.displayName,
                            "",
                            "&fPrice: &6${cost}g",
                            "",
                            "&7Click to upgrade!"
                        )
                    )

                }
            }
        }
    }
}