package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.data.repository.MongoPlayerRepository
import me.roustytousty.elytrapvp.services.event.EventService
import me.roustytousty.elytrapvp.services.mapreset.MapResetService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.scoreboard.ScoreboardService
import me.roustytousty.elytrapvp.services.shop.ShopService

object Services {

    private lateinit var playerRepository: PlayerRepository

    private lateinit var playerCache: PlayerCache

    lateinit var playerService: PlayerService
        private set

    lateinit var mapResetService: MapResetService
        private set

    lateinit var eventService: EventService
        private set

    lateinit var shopService: ShopService
        private set

    lateinit var scoreboardService: ScoreboardService
        private set



    fun init() {

        playerRepository = MongoPlayerRepository()
        playerCache = PlayerCache()

        playerService = PlayerService(
            repository = playerRepository,
            cache = playerCache
        )

        mapResetService = MapResetService()

        eventService = EventService()

        shopService = ShopService(
            playerService = playerService
        )

        scoreboardService = ScoreboardService(
            playerService = playerService,
            mapResetService = mapResetService,
            eventService = eventService
        )
    }
}