package me.roustytousty.elytrapvp.gui.rebirth

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
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

class RebirthMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Rebirth") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            13 -> {
                Services.rebirthService.tryPlayerRebirth(p)
                p.closeInventory()
            }
            18 -> {
                p.closeInventory()
                SoundUtils.playGuiClick(p)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Rebirth") {
            e.isCancelled = true
        }
    }

    companion object {

        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Rebirth")
            initItems(player, inventory)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(player: Player, inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val playerData = Services.playerService.getOrCreatePlayerData(player)
            val currentCost = Services.rebirthService.getRebirthCost(playerData.rebirths)
            val shardReward = Services.rebirthService.getShardReward(playerData.rebirths) // Get current reward

            val nextMultiplier = 1.0 + ((playerData.rebirths + 1) * 0.2)

            val upgradeLines = UpgradeType.values().map { type ->
                val maxed = Services.upgradeService.getNextUpgradeData(playerData, type).maxed
                val icon = if (maxed) "&a✔" else "&c✘"
                "$icon &7${type.name.lowercase().replace("_", " ")} maxed"
            }.toMutableList()

            val goldIcon = if (playerData.gold >= currentCost) "&a✔" else "&c✘"
            upgradeLines.add("$goldIcon &7${currentCost} gold")

            val totalReqs = UpgradeType.values().size + 1
            val metReqs = upgradeLines.count { it.startsWith("&a") }

            inventory.setItem(13, itemBuilder(
                Material.CHERRY_SAPLING, 1, false, "&eRebirth",
                "&fReset progress and gain:",
                "&7+$shardReward Shards",
                "&7+${String.format("%.1f", nextMultiplier)}x Gold Multiplier",
                "",
                "&fYou will lose:",
                "&7All upgrades",
                "&7All gold",
                "",
                "&fRequirements:",
                "&fProgress: &6$metReqs&f/&6$totalReqs",
                *upgradeLines.toTypedArray(),
                "",
                "&7Click to rebirth!"
            ))

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