package me.roustytousty.elytrapvp.services.quest

import org.bukkit.Material

data class Quest(
    val id: String,
    val type: QuestType,
    val difficulty: QuestDifficulty,
    val rewardType: QuestRewardType,
    val rewardAmount: Int,
    val goal: Int,
    val description: String,
    val targetMaterial: Material? = null
)