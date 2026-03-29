package me.roustytousty.elytrapvp.services.leaderboard

import me.roustytousty.elytrapvp.data.cache.LeaderboardCache
import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.services.player.PlayerService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class LeaderboardService(
    private val playerRepository: PlayerRepository,
    private val leaderboardCache: LeaderboardCache,
    private val playerService: PlayerService,
    private val plugin: JavaPlugin
) {

    private val benchmarks = listOf(150, 200, 500, 1000, 2000, 5000, 10000)

    private val trackedStats = listOf(
        "kills",
        "gold",
        "deaths",
        "recordKillstreak",
        "rebirths"
    )

    init {
        start()
    }

    private fun start() {

        Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            Runnable { refreshLeaderboards() },
            0L,
            20L * 60
        )
    }

    private fun refreshLeaderboards() {
        trackedStats.forEach { stat ->
            val topPlayers = playerRepository.getTopLeaderboard(stat, 100)

            val thresholdMap = mutableMapOf<Int, Int>()
            benchmarks.forEach { rank ->
                thresholdMap[rank] = playerRepository.getValueAtOffset(stat, rank)
            }

            leaderboardCache.update(stat, topPlayers, thresholdMap)
        }
    }

    fun getRankDisplay(stat: String, player: Player): String {
        val cachedRank = leaderboardCache.getRank(stat, player.uniqueId)
        if (cachedRank != -1) return "#$cachedRank"

        val playerValue = playerService.getDynamicPlayerData(player, stat) as Int

        if (playerValue <= 0) return "Unranked"

        val statThresholds = leaderboardCache.getThresholds(stat)

        for (limit in benchmarks) {
            val requiredValue = statThresholds[limit] ?: 0

            if (requiredValue == 0 || playerValue >= requiredValue) {
                return "#<$limit"
            }
        }

        return "#10000+"
    }

    fun getTop(stat: String) : List<LeaderboardEntry> {
        return leaderboardCache.getTop(stat)
    }

    fun getRank(stat: String, player: Player) : Int {
        val uuid = player.uniqueId

        return leaderboardCache.getRank(stat, uuid)
    }
}