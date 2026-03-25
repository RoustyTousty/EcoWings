package me.roustytousty.elytrapvp.services.shop

import me.roustytousty.elytrapvp.data.configs.ShopConfig
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.MiscUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopService(
    private val playerService: PlayerService
) {
    private val shopItems = mutableMapOf<String, MutableList<ShopItem>>()

    init {
        loadItems()
    }

    fun loadItems() {
        shopItems.clear()
        val config = ShopConfig.getConfig()
        val shopsSection = config.getConfigurationSection("shops") ?: return

        for (shopType in shopsSection.getKeys(false)) {
            val itemsSection = shopsSection.getConfigurationSection(shopType) ?: continue

            for (itemKey in itemsSection.getKeys(false)) {
                try {
                    val slot = itemsSection.getInt("$itemKey.slot")
                    val materialStr = itemsSection.getString("$itemKey.material") ?: continue
                    val material = Material.valueOf(materialStr.uppercase())
                    val amount = itemsSection.getInt("$itemKey.amount", 1)
                    val cost = itemsSection.getInt("$itemKey.cost", 0)
                    val displayName = itemsSection.getString("$itemKey.display_name") ?: ""
                    val description = itemsSection.getStringList("$itemKey.description")

                    val item = ShopItem(shopType, slot, material, amount, cost, displayName, description)

                    shopItems.computeIfAbsent(shopType) { mutableListOf() }.add(item)
                } catch (e: Exception) {
                    println("Failed to load shop item '$itemKey' in category '$shopType'.")
                    e.printStackTrace()
                }
            }
        }
    }

    fun getItems(shopType: String): List<ShopItem> {
        return shopItems[shopType] ?: emptyList()
    }

    fun getItemBySlot(shopType: String, slot: Int): ShopItem? {
        return getItems(shopType).find { it.slot == slot }
    }

    fun shopPurchaseItem(player: Player, item: ShopItem) {
        val playerData = playerService.getOrCreatePlayerData(player)

        if (playerData.gold < item.cost) {
            MessageUtils.sendError(player, "&fNot enough gold! You need &6&l${item.cost}g&f!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        val itemStack = ItemStack(item.material, item.amount)
        val meta = itemStack.itemMeta
        if (meta != null) {
            meta.setDisplayName(parse("&f${item.displayName}"))
            itemStack.itemMeta = meta
        }

        if (!MiscUtils.hasInventorySpaceForItemStack(player, itemStack)) {
            MessageUtils.sendError(player, "&fNot enough space in your inventory!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        playerData.gold -= item.cost
        player.inventory.addItem(itemStack)
        MessageUtils.sendSuccess(player, "&fYou purchased &6&l${item.displayName} &ffor &6&l${item.cost}g&f!")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }
}