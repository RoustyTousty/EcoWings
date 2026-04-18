package me.roustytousty.elytrapvp.gui.cosmetics

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.cosmetic.*
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

class ConfirmCosmeticPurchaseMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val holder = e.inventory.holder
        if (holder !is ConfirmCosmeticPurchaseHolder) return

        e.isCancelled = true
        val p = e.whoClicked as Player
        val service = Services.cosmeticService

        when (e.rawSlot) {
            0 -> {
                CosmeticSelectMenu.openInventory(p, holder.type)
                SoundUtils.playGuiClick(p)
            }
            8 -> {
                val success = when (holder.type) {
                    CosmeticType.PATTERN -> service.tryPlayerPurchaseTrimPattern(p, holder.item as CosmeticPattern)
                    CosmeticType.MATERIAL -> service.tryPlayerPurchaseTrimMaterial(p, holder.item as CosmeticMaterial)
                    CosmeticType.COLOR -> service.tryPlayerPurchaseColor(p, holder.item as CosmeticColor)
                }

                if (success) {
                    SoundUtils.playSuccess(p)
                    MessageUtils.sendSuccess(p, "&fYou successfully unlocked &6${holder.item.displayName}&f!")
                    CosmeticSelectMenu.openInventory(p, holder.type)
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
        if (e.inventory.holder is ConfirmCosmeticPurchaseHolder) {
            e.isCancelled = true
        }
    }

    companion object {
        fun openInventory(player: Player, item: ICosmetic, type: CosmeticType) {
            val holder = ConfirmCosmeticPurchaseHolder(item, type)
            val inventory: Inventory = Bukkit.createInventory(holder, 9, "Confirm Purchase")

            holder.setInventory(inventory)
            initItems(inventory, item)

            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, item: ICosmetic) {
            val fillerSlots = intArrayOf(1, 2, 3, 5, 6, 7)
            for (slot in fillerSlots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(0, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cCancel"))
            inventory.setItem(4, itemBuilder(item.icon, 1, false, "&e${item.displayName}"))

            val confirmLore = mutableListOf<String>()
            confirmLore.add("")
            if (item.goldCost > 0) confirmLore.add("&fCost: &6${item.goldCost} Gold")
            if (item.shardCost > 0) confirmLore.add("&fCost: &b${item.shardCost} Shards")
            confirmLore.add("")
            confirmLore.add("&7Click to confirm purchase!")

            inventory.setItem(8, itemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, false, "&aConfirm", *confirmLore.toTypedArray()))
        }
    }
}