package me.roustytousty.elytrapvp.data.repository

import me.roustytousty.elytrapvp.data.model.LeaderboardEntry
import me.roustytousty.elytrapvp.data.model.PlayerData
import java.util.*

interface PlayerRepository {

    fun loadPlayer(uuid: UUID): PlayerData?

    fun savePlayer(playerData: PlayerData)

    fun createPlayer(uuid: UUID, username: String): PlayerData

    fun getTopLeaderboard(stat: String, limit: Int): List<LeaderboardEntry>

    fun getPlayerRank(uuid: UUID, stat: String): Int
}