package me.roustytousty.elytrapvp.services.cosmetic

import org.bukkit.Color
import org.bukkit.Material

enum class CosmeticColor(
    override val id: String,
    override val displayName: String,
    override val goldCost: Int,
    override val shardCost: Int,
    override val requiresEco: Boolean,
    override val icon: Material,
    val bukkitColor: Color
) : ICosmetic {
    WHITE("white", "White", 500, 0, false, Material.WHITE_DYE, Color.WHITE),
    ORANGE("orange", "Orange", 500, 0, false, Material.ORANGE_DYE, Color.ORANGE),
    MAGENTA("magenta", "Magenta", 500, 0, false, Material.MAGENTA_DYE, Color.FUCHSIA),
    LIGHT_BLUE("light_blue", "Light Blue", 500, 0, false, Material.LIGHT_BLUE_DYE, Color.AQUA),
    YELLOW("yellow", "Yellow", 500, 0, false, Material.YELLOW_DYE, Color.YELLOW),
    LIME("lime", "Lime", 500, 0, false, Material.LIME_DYE, Color.LIME),
    PINK("pink", "Pink", 500, 0, false, Material.PINK_DYE, Color.fromRGB(255, 105, 180)),
    GRAY("gray", "Gray", 500, 0, false, Material.GRAY_DYE, Color.GRAY),
    LIGHT_GRAY("light_gray", "Light Gray", 500, 0, false, Material.LIGHT_GRAY_DYE, Color.SILVER),
    CYAN("cyan", "Cyan", 500, 0, false, Material.CYAN_DYE, Color.TEAL),
    PURPLE("purple", "Purple", 500, 0, false, Material.PURPLE_DYE, Color.PURPLE),
    BLUE("blue", "Blue", 500, 0, false, Material.BLUE_DYE, Color.BLUE),
    BROWN("brown", "Brown", 500, 0, false, Material.BROWN_DYE, Color.fromRGB(139, 69, 19)),
    GREEN("green", "Green", 500, 0, false, Material.GREEN_DYE, Color.GREEN),
    RED("red", "Red", 500, 0, false, Material.RED_DYE, Color.RED),
    BLACK("black", "Black", 500, 0, false, Material.BLACK_DYE, Color.BLACK);

    companion object {
        fun fromId(id: String): CosmeticColor? = values().find { it.id == id }
        fun getSortedColors(): List<CosmeticColor> = values().toList()
    }
}