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

class PerkSelectMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val title = e.view.title
        if (!title.startsWith("Select Perk - Slot ")) return

        e.isCancelled = true
        val clickedItem = e.currentItem ?: return
        if (clickedItem.type.isAir) return

        val p = e.whoClicked as Player
        val slotIndex = title.last().digitToIntOrNull()?.minus(1) ?: return

        when (e.rawSlot) {
            10 -> {
                Services.perkService.unequipPerk(p, slotIndex)
                SoundUtils.playSuccess(p)
                PerksMenu.openInventory(p)
                return
            }
            45 -> {
                PerksMenu.openInventory(p)
                SoundUtils.playGuiClick(p)
                return
            }
        }

        val perks = PerkType.getSortedPerks()
        val index = e.rawSlot - 11
        if (index < 0 || index >= perks.size) return
        val perk = perks[index]

        if (Services.perkService.hasUnlockedPerk(p, perk)) {
            if (Services.perkService.equipPerk(p, slotIndex, perk)) {
                SoundUtils.playSuccess(p)
                PerksMenu.openInventory(p)
            } else {
                MessageUtils.sendError(p, "&cYou already have this perk equipped!")
                SoundUtils.playFailure(p)
            }
        } else {
            if (perk.requiresEco && !p.hasPermission("elytrapvp.rank.eco")) {
                MessageUtils.sendError(p, "You need the &6Eco &crank to unlock this perk!")
                SoundUtils.playFailure(p)
                return
            }
            ConfirmPerkPurchaseMenu.openInventory(p, perk, slotIndex)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title.startsWith("Select Perk - Slot ")) e.isCancelled = true
    }

    companion object {
        fun openInventory(player: Player, slotIndex: Int) {
            val inventory = Bukkit.createInventory(null, 54, "Select Perk - Slot ${slotIndex + 1}")
            initItems(inventory, player)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(45, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))
            inventory.setItem(10, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cRemove Perk", "&7Click to clear this slot."))

            val perks = PerkType.getSortedPerks()
            val playerData = Services.playerService.getOrCreatePlayerData(player)
            var currentSlot = 11

            for (perk in perks) {
                if (currentSlot % 9 == 8) currentSlot += 3

                val isUnlocked = Services.perkService.hasUnlockedPerk(player, perk)
                val isEquipped = playerData.equippedPerks.contains(perk.id)

                val material = when {
                    isUnlocked -> perk.icon
                    perk.requiresEco -> Material.ORANGE_STAINED_GLASS_PANE
                    else -> Material.GRAY_STAINED_GLASS_PANE
                }

                val lore = mutableListOf<String>()
                lore.addAll(perk.description)
                lore.add("")

                if (isUnlocked) {
                    lore.add(if (isEquipped) "&aEquipped!" else "&eClick to equip!")
                } else {
                    if (perk.goldCost > 0) lore.add("&fCost: &6${perk.goldCost} Gold")
                    if (perk.shardCost > 0) lore.add("&fCost: &b${perk.shardCost} Shards")
                    if (perk.requiresEco) lore.add("&cRequires Eco Rank!")
                    lore.add("")
                    lore.add("&7Click to purchase!")
                }

                inventory.setItem(currentSlot, itemBuilder(material, 1, isEquipped, "&f${perk.displayName}", *lore.toTypedArray()))
                currentSlot++
            }
        }
    }
}