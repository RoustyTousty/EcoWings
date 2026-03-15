package me.roustytousty.elytrapvp.data.cache

import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.model.PlayerData
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LeaderboardCache {

    private val leaderboards: MutableMap<String, List<LeaderboardEntry>> = ConcurrentHashMap()
    private val ranks: MutableMap<String, MutableMap<UUID, Int>> = ConcurrentHashMap()

    fun update(stat: String, players: List<LeaderboardEntry>) {

        leaderboards[stat] = players

        val rankMap = mutableMapOf<UUID, Int>()

        players.forEachIndexed { index, player ->
            rankMap[player.uuid] = index + 1
        }

        ranks[stat] = rankMap
    }

    fun getTop(stat: String): List<LeaderboardEntry> {
        return leaderboards[stat] ?: emptyList()
    }

    fun getRank(stat: String, uuid: UUID): Int {
        return ranks[stat]?.get(uuid) ?: -1
    }
}