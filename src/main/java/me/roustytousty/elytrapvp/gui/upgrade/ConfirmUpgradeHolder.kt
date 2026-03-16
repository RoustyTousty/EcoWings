package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.services.shop.UpgradeType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class ConfirmUpgradeHolder(
    val type: UpgradeType
) : InventoryHolder {

    private lateinit var inventory: Inventory

    fun setInventory(inv: Inventory) {
        this.inventory = inv
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}