package me.roustytousty.elytrapvp.gui.cosmetics

import me.roustytousty.elytrapvp.services.cosmetic.CosmeticType
import me.roustytousty.elytrapvp.services.cosmetic.ICosmetic
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class ConfirmCosmeticPurchaseHolder(
    val item: ICosmetic,
    val type: CosmeticType
) : InventoryHolder {

    private lateinit var inventory: Inventory

    fun setInventory(inv: Inventory) {
        this.inventory = inv
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}