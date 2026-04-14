package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.cache.LeaderboardCache
import me.roustytousty.elytrapvp.data.cache.PlayerCache
import me.roustytousty.elytrapvp.data.repository.EventRepository
import me.roustytousty.elytrapvp.data.repository.MongoEventRepository
import me.roustytousty.elytrapvp.data.repository.PlayerRepository
import me.roustytousty.elytrapvp.data.repository.MongoPlayerRepository
import me.roustytousty.elytrapvp.services.announcement.AnnouncementService
import me.roustytousty.elytrapvp.services.combat.CombatService
import me.roustytousty.elytrapvp.services.currency.CurrencyService
import me.roustytousty.elytrapvp.services.event.EventService
import me.roustytousty.elytrapvp.services.gold.GoldSpawnService
import me.roustytousty.elytrapvp.services.kit.KitService
import me.roustytousty.elytrapvp.services.leaderboard.LeaderboardService
import me.roustytousty.elytrapvp.services.map.MapService
import me.roustytousty.elytrapvp.services.perk.PerkService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.punishment.PunishmentService
import me.roustytousty.elytrapvp.services.rebirth.RebirthService
import me.roustytousty.elytrapvp.services.region.RegionService
import me.roustytousty.elytrapvp.services.scoreboard.ScoreboardService
import me.roustytousty.elytrapvp.services.shop.ShopService
import me.roustytousty.elytrapvp.services.tablist.TablistService
import me.roustytousty.elytrapvp.services.upgrade.UpgradeService
import org.bukkit.plugin.java.JavaPlugin

object Services {

    lateinit var plugin: JavaPlugin
        private set

    private lateinit var playerRepository: PlayerRepository
    private lateinit var eventRepository: EventRepository

    private lateinit var playerCache: PlayerCache
    private lateinit var leaderboardCache: LeaderboardCache

    lateinit var playerService: PlayerService
        private set

    lateinit var leaderboardService: LeaderboardService
        private set

    lateinit var punishmentService: PunishmentService
        private set

    lateinit var mapService: MapService
        private set

    lateinit var eventService: EventService
        private set

    lateinit var shopService: ShopService
        private set

    lateinit var goldSpawnService: GoldSpawnService
        private set

    lateinit var currencyService: CurrencyService
        private set

    lateinit var upgradeService: UpgradeService
        private set

    lateinit var perkService: PerkService
        private set

    lateinit var rebirthService: RebirthService
        private set

    lateinit var kitService: KitService
        private set

    lateinit var regionService: RegionService
        private set

    lateinit var combatService: CombatService
        private set

    lateinit var scoreboardService: ScoreboardService
        private set

    lateinit var tablistService: TablistService
        private set

    lateinit var announcementService: AnnouncementService
        private set



    fun init(pluginInstance: JavaPlugin) {
        plugin = pluginInstance

        playerRepository = MongoPlayerRepository()
        eventRepository = MongoEventRepository()
        playerCache = PlayerCache()
        leaderboardCache = LeaderboardCache()


        tablistService = TablistService()
        regionService = RegionService()
        punishmentService = PunishmentService()

        mapService = MapService(
            regionService = regionService
        )

        goldSpawnService = GoldSpawnService(
            regionService = regionService,
            plugin = plugin
        )

        playerService = PlayerService(
            repository = playerRepository,
            cache = playerCache
        )

        eventService = EventService(
            eventRepository = eventRepository,
            playerService = playerService,
            plugin = plugin
        )

        currencyService = CurrencyService(
            playerService = playerService
        )

        announcementService = AnnouncementService(
            plugin = plugin
        )

        leaderboardService = LeaderboardService(
            playerRepository = playerRepository,
            leaderboardCache = leaderboardCache,
            playerService = playerService,
            plugin = plugin
        )

        shopService = ShopService(
            playerService = playerService
        )

        upgradeService = UpgradeService(
            playerService = playerService,
            plugin = plugin
        )

        perkService = PerkService(
            playerService = playerService
        )

        kitService = KitService(
            playerService = playerService,
            upgradeService = upgradeService,
            shopService = shopService,
            plugin = plugin
        )

        rebirthService = RebirthService(
            playerService = playerService,
            upgradeService = upgradeService,
            kitService = kitService
        )

        scoreboardService = ScoreboardService(
            playerService = playerService,
            mapService = mapService,
            eventService = eventService
        )

        combatService = CombatService(
            regionService = regionService,
            plugin = plugin
        )
    }
}