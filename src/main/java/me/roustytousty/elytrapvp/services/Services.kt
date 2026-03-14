package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.data.repository.MongoPlayerRepository
import me.roustytousty.elytrapvp.services.player.PlayerService

object Services {

    private lateinit var playerRepository: PlayerRepository

    private lateinit var playerCache: PlayerCache

    lateinit var playerService: PlayerService
        private set

    fun init() {

        playerRepository = MongoPlayerRepository()
        playerCache = PlayerCache()

        playerService = PlayerService(
            repository = playerRepository,
            cache = playerCache
        )
    }
}