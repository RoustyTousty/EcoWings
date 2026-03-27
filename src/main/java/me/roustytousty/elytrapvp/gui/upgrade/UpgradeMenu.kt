package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
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

        if (e.rawSlot == 45) {
            p.closeInventory()
            SoundUtils.playGuiClick(p)
            return
        }

        val type = UpgradeType.values().firstOrNull { it.slot == e.rawSlot } ?: return

        val playerData = Services.playerService.getOrCreatePlayerData(p)
        val next = Services.upgradeService.getNextUpgradeData(playerData, type)

        if (next.maxed) {
            MessageUtils.sendError(p, "&fThis item is already &c&lMAXED &fand cannot be upgraded further.")
            SoundUtils.playFailure(p)
            return
        }

        ConfirmUpgradeMenu.openInventory(p, type)
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Upgrade") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 54, "Upgrade")
            initItems(inventory, player)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory, player: Player) {

            val playerData = Services.playerService.getOrCreatePlayerData(player)

            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inventory.setItem(slot,
                    itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            for (type in UpgradeType.values()) {

                val current = Services.upgradeService.getCurrentUpgradeData(playerData, type)
                val next = Services.upgradeService.getNextUpgradeData(playerData, type)

                if (next.maxed) {

                    inventory.setItem(
                        type.slot,
                        itemBuilder(
                            current.item,
                            "${current.item.itemMeta?.displayName} &c&lMAXED"
                        )
                    )

                } else if (current.item.type == Material.AIR) {

                    inventory.setItem(
                        type.slot,
                        itemBuilder(
                            Material.GRAY_STAINED_GLASS_PANE,
                            1,
                            false,
                            "&f${type.displayName} &6[&fT0&6]",
                            "",
                            "&fUpgrade: &6${next.cost}g",
                            "",
                            "&7Click to upgrade!"
                        )
                    )

                } else {

                    inventory.setItem(
                        type.slot,
                        itemBuilder(
                            current.item,
                            null,
                            "",
                            "&fUpgrade: &6${next.cost}g",
                            "",
                            "&7Click to upgrade!"
                        )
                    )
                }
            }

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