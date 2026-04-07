package me.roustytousty.elytrapvp.gui.perks

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.perk.PerkType
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
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
        val playerData = Services.playerService.getOrCreatePlayerData(p)

        when (e.rawSlot) {
            20 -> PerkSelectMenu.openInventory(p, 0) // Slot 1 (Index 0)
            22 -> { // Slot 2 (Index 1)
                if (playerData.unlockedPerkSlots > 1) {
                    PerkSelectMenu.openInventory(p, 1)
                } else {
                    if (Services.perkService.tryUnlockSlot(p, 1)) {
                        SoundUtils.playSuccess(p)
                        openInventory(p)
                    } else {
                        MessageUtils.sendError(p, "You don't have enough Shards to unlock this slot!")
                        SoundUtils.playFailure(p)
                    }
                }
            }
            24 -> {
                if (playerData.unlockedPerkSlots > 2) {
                    PerkSelectMenu.openInventory(p, 2)
                } else if (playerData.unlockedPerkSlots == 2) {
                    if (!p.hasPermission("elytrapvp.rank.eco")) {
                        MessageUtils.sendError(p, "You need the &6Eco &crank to unlock this slot!")
                        SoundUtils.playFailure(p)
                        return
                    }
                    if (Services.perkService.tryUnlockSlot(p, 2)) {
                        SoundUtils.playSuccess(p)
                        openInventory(p)
                    } else {
                        MessageUtils.sendError(p, "You don't have enough Shards to unlock this slot!")
                        SoundUtils.playFailure(p)
                    }
                } else {
                    MessageUtils.sendError(p, "You must unlock Slot 2 first!")
                    SoundUtils.playFailure(p)
                }
            }
            45 -> {
                p.closeInventory()
                SoundUtils.playGuiClick(p)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Perks") e.isCancelled = true
    }

    companion object {
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 54, "Perks")
            initItems(inventory, player)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val playerData = Services.playerService.getOrCreatePlayerData(player)

            val perk1 = PerkType.fromId(playerData.equippedPerks.getOrElse(0) { "" })
            inventory.setItem(20, itemBuilder(
                perk1?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, perk1 != null,
                perk1?.displayName ?: "&aSlot 1", "&7Click to select a perk!"
            ))

            if (playerData.unlockedPerkSlots > 1) {
                val perk2 = PerkType.fromId(playerData.equippedPerks.getOrElse(1) { "" })
                inventory.setItem(22, itemBuilder(perk2?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, perk2 != null, perk2?.displayName ?: "&aSlot 2", "&7Click to select a perk!"))
            } else {
                inventory.setItem(22, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cLocked Slot 2", "&fCost: &b${Services.perkService.slot2Cost} Shards", "&7Click to purchase!"))
            }

            if (playerData.unlockedPerkSlots > 2) {
                val perk3 = PerkType.fromId(playerData.equippedPerks.getOrElse(2) { "" })
                inventory.setItem(24, itemBuilder(perk3?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, perk3 != null, perk3?.displayName ?: "&aSlot 3", "&7Click to select a perk!"))
            } else {
                inventory.setItem(24, itemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 1, false, "&6Locked Slot 3", "&fCost: &b${Services.perkService.slot3Cost} Shards", "&cRequires Eco Rank!", "&7Click to purchase!"))
            }

            inventory.setItem(45, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cClose"))
        }
    }
}