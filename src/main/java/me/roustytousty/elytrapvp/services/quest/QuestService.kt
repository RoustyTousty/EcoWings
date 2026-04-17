package me.roustytousty.elytrapvp.services.quest

import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class QuestService {

    private val allQuests = listOf(

        // === NORMAL QUESTS (Gold Rewards) ===

        // Death
        Quest("n_death_25", QuestType.DEATHS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 100, 25, "Die 25 times"),
        Quest("n_death_50", QuestType.DEATHS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 200, 50, "Die 50 times"),
        Quest("n_death_80", QuestType.DEATHS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 350, 80, "Die 80 times"),

        // Killing
        Quest("n_kill_5", QuestType.KILL_PLAYERS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 100, 5, "Kill 5 Players"),
        Quest("n_kill_10", QuestType.KILL_PLAYERS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 150, 10, "Kill 10 Players"),
        Quest("n_kill_15", QuestType.KILL_PLAYERS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 250, 15, "Kill 15 Players"),
        Quest("n_kill_20", QuestType.KILL_PLAYERS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 300, 20, "Kill 20 Players"),
        Quest("n_kill_25", QuestType.KILL_PLAYERS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 400, 25, "Kill 25 Players"),

        // Placing
        Quest("n_place_obsidian_5", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 300, 5, "Place 5 Obsidian", Material.OBSIDIAN),
        Quest("n_place_obsidian_10", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 600, 10, "Place 10 Obsidian", Material.OBSIDIAN),
        Quest("n_place_oak_planks_64", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 150, 64, "Place 64 Oak Planks", Material.OAK_PLANKS),
        Quest("n_place_oak_planks_128", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 300, 128, "Place 128 Oak Planks", Material.OAK_PLANKS),
        Quest("n_place_white_wool_256", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 200, 256, "Place 256 White Wool", Material.WHITE_WOOL),
        Quest("n_place_white_wool_512", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 400, 512, "Place 512 White Wool", Material.WHITE_WOOL),
        Quest("n_place_stone_bricks_64", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 250, 64, "Place 64 Stone Bricks", Material.STONE_BRICKS),
        Quest("n_place_stone_bricks_128", QuestType.PLACE_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 500, 128, "Place 128 Stone Bricks", Material.STONE_BRICKS),

        // Breaking
        Quest("n_break_obsidian_5", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 300, 5, "Break 5 Obsidian", Material.OBSIDIAN),
        Quest("n_break_obsidian_10", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 600, 10, "Break 10 Obsidian", Material.OBSIDIAN),
        Quest("n_break_oak_planks_64", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 150, 64, "Break 64 Oak Planks", Material.OAK_PLANKS),
        Quest("n_break_oak_planks_128", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 300, 128, "Break 128 Oak Planks", Material.OAK_PLANKS),
        Quest("n_break_white_wool_128", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 200, 128, "Break 128 White Wool", Material.WHITE_WOOL),
        Quest("n_break_white_wool_256", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 400, 256, "Break 256 White Wool", Material.WHITE_WOOL),
        Quest("n_break_stone_bricks_64", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 250, 64, "Break 64 Stone Bricks", Material.STONE_BRICKS),
        Quest("n_break_stone_bricks_128", QuestType.BREAK_BLOCKS, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 500, 128, "Break 128 Stone Bricks", Material.STONE_BRICKS),

        // Flying
        Quest("n_fly_1k", QuestType.FLY_DISTANCE, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 200, 1000, "Fly 1,000 blocks"),
        Quest("n_fly_3k", QuestType.FLY_DISTANCE, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 350, 3000, "Fly 3,000 blocks"),
        Quest("n_fly_5k", QuestType.FLY_DISTANCE, QuestDifficulty.NORMAL, QuestRewardType.GOLD, 500, 5000, "Fly 5,000 blocks"),

        // === HARD QUESTS (Shard Rewards) ===

        // Death
        Quest("h_death_100", QuestType.DEATHS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 100, "Die 100 times"),
        Quest("h_death_150", QuestType.DEATHS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 150, "Die 150 times"),

        // Killing
        Quest("h_kill_50", QuestType.KILL_PLAYERS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 50, "Kill 50 Players"),
        Quest("h_kill_70", QuestType.KILL_PLAYERS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 70, "Kill 70 Players"),

        // Placing
        Quest("h_place_obsidian", QuestType.PLACE_BLOCKS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 30, "Place 30 Obsidian", Material.OBSIDIAN),
        Quest("h_place_white_wool", QuestType.PLACE_BLOCKS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 1280, "Place 1280 White Wool", Material.WHITE_WOOL),

        // Breaking
        Quest("h_break_obsidian", QuestType.BREAK_BLOCKS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 30, "Break 20 Obsidian", Material.OBSIDIAN),
        Quest("h_break_white_wool", QuestType.BREAK_BLOCKS, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 1280, "Break 1280 White Wool", Material.WHITE_WOOL),

        // Flying
        Quest("h_fly_10k", QuestType.FLY_DISTANCE, QuestDifficulty.HARD, QuestRewardType.SHARD, 1, 10000, "Fly 10,000 blocks"),
    )

    fun getDailyQuests(): List<Quest> {
        val seed = LocalDate.now().toEpochDay()
        val random = kotlin.random.Random(seed)

        val hardPool = allQuests.filter { it.difficulty == QuestDifficulty.HARD }
        val normalPool = allQuests.filter { it.difficulty == QuestDifficulty.NORMAL }

        val dailyHard = hardPool.random(random)
        val dailyNormal = normalPool.shuffled(random).take(2)

        return listOf(dailyHard) + dailyNormal
    }

    fun checkAndResetDailyData(playerData: PlayerData) {
        val today = LocalDate.now().toEpochDay()
        if (playerData.lastQuestDate < today) {
            playerData.lastQuestDate = today
            playerData.questProgress.clear()
            playerData.claimedQuests.clear()
        }
    }

    fun handleProgress(player: Player, type: QuestType, amount: Int, material: Material? = null) {
        val playerData = Services.playerService.getOrCreatePlayerData(player)
        checkAndResetDailyData(playerData)

        for (quest in getDailyQuests()) {
            if (quest.type != type) continue
            if (quest.targetMaterial != null && quest.targetMaterial != material) continue

            val current = playerData.questProgress.getOrDefault(quest.id, 0)
            if (current >= quest.goal) continue

            val next = minOf(current + amount, quest.goal)
            playerData.questProgress[quest.id] = next

            if (next >= quest.goal) {
                MessageUtils.sendMessage(player, "&a&lQUEST COMPLETE! &fYou finished: &6${quest.description}")
            }
        }
    }

    fun claimReward(player: Player, questId: String): Boolean {
        val playerData = Services.playerService.getOrCreatePlayerData(player)
        val quest = getDailyQuests().find { it.id == questId } ?: return false

        if (playerData.questProgress.getOrDefault(questId, 0) < quest.goal) return false
        if (playerData.claimedQuests.contains(questId)) return false

        playerData.claimedQuests.add(questId)

        if (quest.rewardType == QuestRewardType.GOLD) {
            Services.currencyService.giveGold(player, quest.rewardAmount, "Quest Reward")
        } else {
            Services.currencyService.giveShards(player, quest.rewardAmount, "Quest Reward")
        }

        MessageUtils.sendMessage(player, "&aClaimed reward for &f${quest.description}!")
        return true
    }

    fun getTimeUntilRefresh(): String {
        val now = LocalDateTime.now()
        val nextDay = now.toLocalDate().plusDays(1).atStartOfDay()
        val duration = Duration.between(now, nextDay)
        return String.format("%02dh %02dm", duration.toHours(), duration.toMinutes() % 60)
    }
}