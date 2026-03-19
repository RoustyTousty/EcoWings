package me.roustytousty.elytrapvp.services.leaderboard

import me.roustytousty.elytrapvp.data.cache.LeaderboardCache
import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class LeaderboardService(
    private val playerRepository: PlayerRepository,
    private val leaderboardCache: LeaderboardCache,
    private val plugin: JavaPlugin
) {

    private val trackedStats = listOf(
        "kills",
        "gold",
        "deaths",
        "recordKillstreak"
    )

    init {
        start()
    }

    private fun start() {

        Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            Runnable { refreshLeaderboards() },
            0L,
            20L * 30
        )
    }

    private fun refreshLeaderboards() {

        trackedStats.forEach { stat ->

            val topPlayers = playerRepository.getTopLeaderboard(stat, 100)

            leaderboardCache.update(stat, topPlayers)
        }
    }

    fun getTop(stat: String) : List<LeaderboardEntry> {
        return leaderboardCache.getTop(stat)
    }

    fun getRank(stat: String, player: Player) : Int {
        val uuid = player.uniqueId

        return leaderboardCache.getRank(stat, uuid)
    }
}