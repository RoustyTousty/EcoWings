package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot

class ScoreboardService {

    private val eventService = ElytraPVP.instance!!.getEventService()
    private val mapResetService = ElytraPVP.instance!!.getMapResetService()

    fun create(player: Player) {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("test", "dummy", parse("  &6&lEcoWings"))
        objective.displaySlot = DisplaySlot.SIDEBAR

        objective.getScore(parse("&f")).score = 10
        objective.getScore(parse(" &fRank: <Member>")).score = 9
        objective.getScore(parse(" &fGold: ")).also {
            board.registerNewTeam("gold").addEntry(it.entry)
        }.score = 8

        objective.getScore(parse("&e")).score = 7

        objective.getScore(parse("&n")).also {
            board.registerNewTeam("dynamic1").addEntry(it.entry)
        }.score = 6
        objective.getScore(parse("&l")).also {
            board.registerNewTeam("dynamic2").addEntry(it.entry)
        }.score = 5

        objective.getScore(parse("&a")).score = 4

        objective.getScore(parse(" &fPlayers: ")).also {
            board.registerNewTeam("players").addEntry(it.entry)
        }.score = 3
        objective.getScore(parse(" &fMap reset: ")).also {
            board.registerNewTeam("mapReset").addEntry(it.entry)
        }.score = 2

        objective.getScore(parse("&3")).score = 1

        objective.getScore(parse("&7ecowings.minehut.gg")).score = 0

        player.scoreboard = board
    }

    fun startUpdateTask() {
        object : BukkitRunnable() {
            override fun run() {
                update()
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0, 20)
    }

    private fun update() {
        for (player in Bukkit.getOnlinePlayers()) {
            val board = player.scoreboard
            val goldTeam = board.getTeam("gold") ?: continue
            val playersTeam = board.getTeam("players") ?: continue
            val mapResetTeam = board.getTeam("mapReset") ?: continue
            val dynamic1Team = board.getTeam("dynamic1") ?: continue
            val dynamic2Team = board.getTeam("dynamic2") ?: continue

            val gold = CacheConfig.getplrVal(player, "gold") as Int
            goldTeam.suffix = parse("&6${formatNumber(gold)}")

            playersTeam.suffix = parse("&6${Bukkit.getOnlinePlayers().size}")
            mapResetTeam.suffix = parse("&6${mapResetService.getFormattedTimeRemaining()}")

            if (eventService.isAnyEventActive()) {
                val activeEvent = eventService.getActiveEvents().first()
                dynamic1Team.prefix = parse("  &8• &fEvent: ")
                dynamic1Team.suffix = parse("&6${activeEvent.name}")

                dynamic2Team.prefix = parse("  &8• &fTime: ")
                dynamic2Team.suffix = parse("&6${eventService.getEventRemainingTime(activeEvent.name)}")
            } else {
                val kills = CacheConfig.getplrVal(player, "kills") as Int
                val killstreak = CacheConfig.getplrVal(player, "killstreak") as Int

                dynamic1Team.prefix = parse("  &8• &fKills: ")
                dynamic1Team.suffix = parse("&6${formatNumber(kills)}")

                dynamic2Team.prefix = parse("  &8• &fKillstreak: ")
                dynamic2Team.suffix = parse("&6${formatNumber(killstreak)}")
            }
        }
    }
}