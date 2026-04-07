package me.roustytousty.elytrapvp.gui.perks

import me.roustytousty.elytrapvp.services.perk.PerkType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class ConfirmPerkPurchaseHolder(
    val perk: PerkType,
    val slotIndex: Int
) : InventoryHolder {

    private lateinit var inventory: Inventory

    fun setInventory(inv: Inventory) {
        this.inventory = inv
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}