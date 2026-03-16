package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.cache.LeaderboardCache
import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.data.repository.MongoPlayerRepository
import me.roustytousty.elytrapvp.services.event.EventService
import me.roustytousty.elytrapvp.services.leaderboard.LeaderboardService
import me.roustytousty.elytrapvp.services.mapreset.MapResetService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.scoreboard.ScoreboardService
import me.roustytousty.elytrapvp.services.shop.ShopService
import me.roustytousty.elytrapvp.services.shop.UpgradeService
import org.bukkit.plugin.java.JavaPlugin

object Services {

    private lateinit var playerRepository: PlayerRepository

    private lateinit var playerCache: PlayerCache
    private lateinit var leaderboardCache: LeaderboardCache

    lateinit var playerService: PlayerService
        private set

    lateinit var leaderboardService: LeaderboardService
        private set

    lateinit var mapResetService: MapResetService
        private set

    lateinit var eventService: EventService
        private set

    lateinit var shopService: ShopService
        private set

    lateinit var upgradeService: UpgradeService
        private set

    lateinit var scoreboardService: ScoreboardService
        private set



    fun init(plugin: JavaPlugin) {

        playerRepository = MongoPlayerRepository()
        playerCache = PlayerCache()
        leaderboardCache = LeaderboardCache()

        playerService = PlayerService(
            repository = playerRepository,
            cache = playerCache
        )

        leaderboardService = LeaderboardService(
            playerRepository = playerRepository,
            leaderboardCache = leaderboardCache,
            plugin = plugin
        )

        mapResetService = MapResetService()

        eventService = EventService()

        shopService = ShopService(
            playerService = playerService
        )

        upgradeService = UpgradeService(
            playerService = playerService
        )

        scoreboardService = ScoreboardService(
            playerService = playerService,
            mapResetService = mapResetService,
            eventService = eventService
        )
    }
}