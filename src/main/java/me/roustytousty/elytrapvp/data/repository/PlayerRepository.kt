package me.roustytousty.elytrapvp.data.repository

import me.roustytousty.elytrapvp.data.model.PlayerData
import java.util.*

interface PlayerRepository {

    fun loadPlayer(uuid: UUID): PlayerData?

    fun savePlayer(player: PlayerData)

    fun createPlayer(uuid: UUID, username: String): PlayerData

    fun getTopPlayers(stat: String, limit: Int): List<PlayerData>

    fun getPlayerRank(uuid: UUID, stat: String): Int
}