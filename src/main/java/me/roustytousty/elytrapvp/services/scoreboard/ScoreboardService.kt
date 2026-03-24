package me.roustytousty.elytrapvp.services.scoreboard

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.mapreset.MapResetService
import me.roustytousty.elytrapvp.services.event.EventService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import java.text.SimpleDateFormat
import java.util.*

class ScoreboardService(
    private val playerService: PlayerService,
    private val mapResetService: MapResetService,
    private val eventService: EventService
) {

    private val ENTRIES = object {
        val PLAYERS = parse(" &fPlayers: ")
        val KILLS = parse("&n")
        val DEATHS = parse("&b")
        val KD = parse("&c")
        val KSTREAK = parse("&l")
    }

    init {
        startUpdateTask()
    }

    fun create(player: Player) {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("test", "dummy", parse("  &6&lEcoWings"))
        objective.displaySlot = DisplaySlot.SIDEBAR

        objective.getScore(parse("&d")).also { board.registerNewTeam("dateTime").addEntry(it.entry) }.score = 15
        objective.getScore(parse("&f")).score = 14
        objective.getScore(parse(" &fRank: <Member>")).score = 13
        objective.getScore(parse(" &fGold: ")).also { board.registerNewTeam("gold").addEntry(it.entry) }.score = 12

        objective.getScore(parse("&e")).score = 11

        board.registerNewTeam("dynamic1").addEntry(ENTRIES.KILLS)
        board.registerNewTeam("dynamic2").addEntry(ENTRIES.DEATHS)
        board.registerNewTeam("dynamic3").addEntry(ENTRIES.KD)
        board.registerNewTeam("dynamic4").addEntry(ENTRIES.KSTREAK)

        objective.getScore(parse("&a")).score = 6

        objective.getScore(ENTRIES.PLAYERS).also { board.registerNewTeam("players").addEntry(it.entry) }.score = 5
        objective.getScore(parse(" &fMap reset: ")).also { board.registerNewTeam("mapReset").addEntry(it.entry) }.score = 4

        objective.getScore(parse("&3")).score = 3
        objective.getScore(parse("&7ecowings.minehut.gg")).score = 2

        player.scoreboard = board
    }

    fun startUpdateTask() {
        object : BukkitRunnable() {
            override fun run() = update()
        }.runTaskTimer(ElytraPVP.instance!!, 0, 20)
    }

    private fun update() {
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy hh:mma")
        val currentDateTime = dateTimeFormat.format(Date())
        val onlineCount = Bukkit.getOnlinePlayers().size

        for (player in Bukkit.getOnlinePlayers()) {
            val board = player.scoreboard
            val objective = board.getObjective("test") ?: continue
            val playerData = playerService.getOrCreatePlayerData(player)

            board.getTeam("dateTime")?.prefix = parse("&7$currentDateTime")
            board.getTeam("gold")?.suffix = parse("&6${formatNumber(playerData.gold)}")
            board.getTeam("mapReset")?.suffix = parse("&6${mapResetService.getFormattedTimeRemaining()}")

            if (onlineCount < 8) {
                board.resetScores(ENTRIES.PLAYERS)
            } else {
                val score = objective.getScore(ENTRIES.PLAYERS)
                if (!score.isScoreSet) score.score = 5
                board.getTeam("players")?.suffix = parse("&6$onlineCount")
            }

            updateDynamicSection(player, board, objective)
        }
    }

    private fun updateDynamicSection(player: Player, board: org.bukkit.scoreboard.Scoreboard, objective: org.bukkit.scoreboard.Objective) {
        val d1 = board.getTeam("dynamic1") ?: return
        val d2 = board.getTeam("dynamic2") ?: return
        val d3 = board.getTeam("dynamic3") ?: return
        val d4 = board.getTeam("dynamic4") ?: return
        val data = playerService.getOrCreatePlayerData(player)

        if (eventService.isAnyEventActive()) {
            // Noņemam liekās rindiņas (KD un Killstreak)
            board.resetScores(ENTRIES.KD)
            board.resetScores(ENTRIES.KSTREAK)

            val activeEvent = eventService.getActiveEvents().first()

            // Izmantojam d1 un d2 priekš Event datiem
            objective.getScore(ENTRIES.KILLS).score = 10
            d1.prefix = parse("  &8• &fEvent: ")
            d1.suffix = parse("&6${activeEvent.name}")

            objective.getScore(ENTRIES.DEATHS).score = 9
            d2.prefix = parse("  &8• &fTime: ")
            d2.suffix = parse("&6${eventService.getEventRemainingTime(activeEvent.name)}")
        } else {
            // Rādām visas 4 rindiņas sakārtotā secībā
            objective.getScore(ENTRIES.KILLS).score = 10
            d1.prefix = parse("  &8• &fKills: ")
            d1.suffix = parse("&6${formatNumber(data.kills)}")

            objective.getScore(ENTRIES.DEATHS).score = 9
            d2.prefix = parse("  &8• &fDeaths: ")
            d2.suffix = parse("&6${formatNumber(data.deaths)}")

            objective.getScore(ENTRIES.KD).score = 8
            val kd = if (data.deaths == 0) data.kills.toDouble() else data.kills.toDouble() / data.deaths
            d3.prefix = parse("  &8• &fK/D: ")
            d3.suffix = parse("&6${"%.2f".format(kd)}")

            objective.getScore(ENTRIES.KSTREAK).score = 7
            d4.prefix = parse("  &8• &fKillstreak: ")
            d4.suffix = parse("&6${formatNumber(data.killstreak)} &8{${formatNumber(data.recordKillstreak)}}")
        }
    }
}