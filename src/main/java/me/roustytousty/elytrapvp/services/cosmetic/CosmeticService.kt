package me.roustytousty.elytrapvp.services.cosmetic

import me.roustytousty.elytrapvp.services.player.PlayerService
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.plugin.java.JavaPlugin

class CosmeticService(
    private val playerService: PlayerService,
    private val plugin: JavaPlugin
) {

    init {
        CosmeticBlockParticleTask().runTaskTimer(plugin, 0L, 2L)
    }

    // --- APPLY COSMETICS ---
    fun applyTrimToItem(player: Player, item: ItemStack) {
        val data = playerService.getOrCreatePlayerData(player)
        val patternId = data.activeTrimPattern
        val materialId = data.activeTrimMaterial

        if (patternId.isEmpty() || materialId.isEmpty()) return

        val pattern = CosmeticPattern.fromId(patternId)?.getBukkitPattern()
        val material = CosmeticMaterial.fromId(materialId)?.getBukkitMaterial()

        if (pattern != null && material != null) {
            val meta = item.itemMeta as? ArmorMeta ?: return
            meta.trim = ArmorTrim(material, pattern)
            item.itemMeta = meta
        }
    }

    fun applyColorToItem(player: Player, item: ItemStack) {
        val data = playerService.getOrCreatePlayerData(player)
        val colorId = data.activeArmorColor

        if (colorId.isEmpty()) return

        val cosmeticColor = CosmeticColor.fromId(colorId) ?: return
        val meta = item.itemMeta as? LeatherArmorMeta ?: return

        meta.setColor(cosmeticColor.bukkitColor)
        item.itemMeta = meta
    }

    // --- UNLOCK CHECKS ---
    fun hasUnlockedPattern(player: Player, pattern: CosmeticPattern): Boolean =
        playerService.getOrCreatePlayerData(player).unlockedTrimPatterns.contains(pattern.id)

    fun hasUnlockedMaterial(player: Player, material: CosmeticMaterial): Boolean =
        playerService.getOrCreatePlayerData(player).unlockedTrimMaterials.contains(material.id)

    fun hasUnlockedColor(player: Player, color: CosmeticColor): Boolean =
        playerService.getOrCreatePlayerData(player).unlockedArmorColors.contains(color.id)


    // --- PURCHASING ---
    fun tryPlayerPurchaseTrimPattern(player: Player, pattern: CosmeticPattern): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        if (hasUnlockedPattern(player, pattern)) return false
        if (pattern.requiresEco && !player.hasPermission("elytrapvp.rank.eco")) return false
        if (data.gold < pattern.goldCost || data.shards < pattern.shardCost) return false

        data.gold -= pattern.goldCost
        data.shards -= pattern.shardCost
        data.unlockedTrimPatterns.add(pattern.id)
        return true
    }

    fun tryPlayerPurchaseTrimMaterial(player: Player, material: CosmeticMaterial): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        if (hasUnlockedMaterial(player, material)) return false
        if (material.requiresEco && !player.hasPermission("elytrapvp.rank.eco")) return false
        if (data.gold < material.goldCost || data.shards < material.shardCost) return false

        data.gold -= material.goldCost
        data.shards -= material.shardCost
        data.unlockedTrimMaterials.add(material.id)
        return true
    }

    fun tryPlayerPurchaseColor(player: Player, color: CosmeticColor): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        if (hasUnlockedColor(player, color)) return false
        if (color.requiresEco && !player.hasPermission("elytrapvp.rank.eco")) return false
        if (data.gold < color.goldCost || data.shards < color.shardCost) return false

        data.gold -= color.goldCost
        data.shards -= color.shardCost
        data.unlockedArmorColors.add(color.id)
        return true
    }

    // --- EQUIPPING ---
    fun equipPattern(player: Player, pattern: CosmeticPattern) {
        playerService.getOrCreatePlayerData(player).activeTrimPattern = pattern.id
    }

    fun equipMaterial(player: Player, material: CosmeticMaterial) {
        playerService.getOrCreatePlayerData(player).activeTrimMaterial = material.id
    }

    fun equipColor(player: Player, color: CosmeticColor) {
        playerService.getOrCreatePlayerData(player).activeArmorColor = color.id
    }

    // --- UNEQUIPPING ---
    fun unequipPattern(player: Player) {
        playerService.getOrCreatePlayerData(player).activeTrimPattern = ""
    }

    fun unequipMaterial(player: Player) {
        playerService.getOrCreatePlayerData(player).activeTrimMaterial = ""
    }

    fun unequipColor(player: Player) {
        playerService.getOrCreatePlayerData(player).activeArmorColor = ""
    }
}