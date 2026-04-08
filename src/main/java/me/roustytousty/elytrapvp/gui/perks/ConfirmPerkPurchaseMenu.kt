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

class ConfirmPerkPurchaseMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val holder = e.inventory.holder
        if (holder !is ConfirmPerkPurchaseHolder) return

        e.isCancelled = true
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            0 -> {
                PerkSelectMenu.openInventory(p, holder.slotIndex)
                SoundUtils.playGuiClick(p)
            }
            8 -> {
                if (Services.perkService.tryPlayerPurchasePerk(p, holder.perk)) {
                    SoundUtils.playSuccess(p)
                    MessageUtils.sendSuccess(p, "&fYou successfully unlocked &6${holder.perk.displayName}&f!")
                    PerkSelectMenu.openInventory(p, holder.slotIndex)
                } else {
                    MessageUtils.sendError(p, "&fPurchase failed! You may lack the funds or rank.")
                    SoundUtils.playFailure(p)
                    p.closeInventory()
                }
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.inventory.holder is ConfirmPerkPurchaseHolder) {
            e.isCancelled = true
        }
    }

    companion object {
        fun openInventory(player: Player, perk: PerkType, slotIndex: Int) {
            val holder = ConfirmPerkPurchaseHolder(perk, slotIndex)
            val inventory: Inventory = Bukkit.createInventory(holder, 9, "Confirm Purchase")

            holder.setInventory(inventory)
            initItems(inventory, perk)

            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, perk: PerkType) {
            val fillerSlots = intArrayOf(1, 2, 3, 5, 6, 7)
            for (slot in fillerSlots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(0, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cCancel"))

            val lore = perk.description.toMutableList()
            inventory.setItem(4, itemBuilder(perk.icon, 1, false, "&e${perk.displayName}", *lore.toTypedArray()))

            val confirmLore = mutableListOf<String>()
            confirmLore.add("")
            if (perk.goldCost > 0) confirmLore.add("&fCost: &6${perk.goldCost} Gold")
            if (perk.shardCost > 0) confirmLore.add("&fCost: &b${perk.shardCost} Shards")
            confirmLore.add("")
            confirmLore.add("&7Click to confirm purchase!")

            inventory.setItem(8, itemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, false, "&aConfirm", *confirmLore.toTypedArray()))
        }
    }
}