package me.roustytousty.elytrapvp.services.shop

import me.roustytousty.elytrapvp.data.configs.ShopConfig
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.MiscUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopService(
    private val playerService: PlayerService
) {

    private val shopItems = mutableMapOf<String, MutableList<ShopItem>>()
    private val materialLookup = mutableMapOf<Material, ShopItem>()

    init {
        loadItems()
    }

    fun loadItems() {
        shopItems.clear()
        materialLookup.clear()

        val config = ShopConfig.getConfig()
        val shopsSection = config.getConfigurationSection("shops") ?: return

        for (shopType in shopsSection.getKeys(false)) {
            val itemsSection = shopsSection.getConfigurationSection(shopType) ?: continue

            for (itemKey in itemsSection.getKeys(false)) {
                try {
                    val materialStr = itemsSection.getString("$itemKey.material") ?: continue
                    val material = Material.valueOf(materialStr.uppercase())

                    val item = ShopItem(
                        shopType = shopType,
                        slot = itemsSection.getInt("$itemKey.slot"),
                        material = material,
                        amount = itemsSection.getInt("$itemKey.amount", 1),
                        cost = itemsSection.getInt("$itemKey.cost", 0),
                        displayName = itemsSection.getString("$itemKey.display_name") ?: "",
                        description = itemsSection.getStringList("$itemKey.description")
                    )

                    shopItems.computeIfAbsent(shopType) { mutableListOf() }.add(item)
                    materialLookup[material] = item
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun isShopItem(material: Material): Boolean = materialLookup.containsKey(material)

    fun isItemInShopType(material: Material, shopType: String): Boolean {
        val item = materialLookup[material] ?: return false
        return item.shopType.equals(shopType, ignoreCase = true)
    }

    fun getShopItems(shopType: String): List<ShopItem> = shopItems[shopType] ?: emptyList()

    fun getShopItemBySlot(shopType: String, slot: Int): ShopItem? = getShopItems(shopType).find { it.slot == slot }

    fun getFormattedItem(material: Material, amount: Int = 1): ItemStack {
        val shopItem = materialLookup[material]
        return if (shopItem != null) {
            buildItemStack(shopItem).apply { this.amount = amount }
        } else {
            ItemStack(material, amount)
        }
    }

    fun giveShopItem(player: Player, item: ShopItem, silent: Boolean = false) {
        if (deliver(player, buildItemStack(item), silent)) {
            if (!silent) {
                MessageUtils.sendSuccess(player, "&fYou received &6&l${item.displayName}&f!")
                SoundUtils.playSuccess(player)
            }
        }
    }

    fun tryPlayerPurchaseItem(player: Player, item: ShopItem) {
        val playerData = playerService.getOrCreatePlayerData(player)

        if (playerData.gold < item.cost) {
            MessageUtils.sendError(player, "&fNot enough gold! You need &6&l${item.cost}g&f!")
            SoundUtils.playFailure(player)
            return
        }

        if (deliver(player, buildItemStack(item), false)) {
            playerData.gold -= item.cost
            MessageUtils.sendSuccess(player, "&fYou purchased &6&l${item.displayName} &ffor &6&l${item.cost}g&f!")
            SoundUtils.playSuccess(player)
        }
    }

    private fun deliver(player: Player, stack: ItemStack, silent: Boolean): Boolean {
        if (!MiscUtils.hasInventorySpaceForItemStack(player, stack)) {
            if (!silent) {
                MessageUtils.sendError(player, "&fNot enough space in your inventory!")
                SoundUtils.playFailure(player)
            }
            return false
        }
        player.inventory.addItem(stack)
        return true
    }

    private fun buildItemStack(item: ShopItem): ItemStack {
        return ItemStack(item.material, item.amount).apply {
            val meta = itemMeta ?: return@apply
            meta.setDisplayName(parse("&f${item.displayName}"))
            itemMeta = meta
        }
    }
}