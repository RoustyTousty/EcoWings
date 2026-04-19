package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.event.EventInterface
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*

class HotPotatoEvent : EventInterface {
    override val name = "Hot Potato"
    override val description = "Be the potato to earn gold!"
    override val displayMaterial = Material.BAKED_POTATO
    override val cost = 450
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    var currentPotatoUuid: UUID? = null
        private set

    private var logicTask: BukkitTask? = null
    private var firstAssignmentMade = false

    private val spawnRegionName = "spawnRegion"
    private val GLOW_EFFECT = PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false, false)

    private val listener = object : Listener {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onPotatoPass(event: EntityDamageByEntityEvent) {
            val victim = event.entity
            val attacker = event.damager

            if (victim is Player && attacker is Player) {
                if (victim.uniqueId == currentPotatoUuid && !CitizensAPI.getNPCRegistry().isNPC(attacker)) {
                    val spawn = Services.regionService.get(spawnRegionName)
                    if (spawn != null && spawn.contains(attacker.location)) return

                    assignPotato(attacker)

                    MessageUtils.sendMessage(victim, "&fYour potato was stolen by &6${attacker.name}!")
                    MessageUtils.sendMessage(attacker, "&6&lHOT POTATO&f! &fYou stole the potato!")
                    attacker.playSound(attacker.location, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
                }
            }
        }

        @EventHandler
        fun onPlayerDeath(event: PlayerDeathEvent) {
            if (event.entity.uniqueId == currentPotatoUuid) {
                removePotatoEffect(event.entity)
                currentPotatoUuid = null
            }
        }

        @EventHandler
        fun onPlayerQuit(event: PlayerQuitEvent) {
            if (event.player.uniqueId == currentPotatoUuid) {
                removePotatoEffect(event.player)
                currentPotatoUuid = null
            }
        }
    }

    override fun activate() {
        isActive = true
        firstAssignmentMade = false
        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)

        logicTask = Bukkit.getScheduler().runTaskTimer(ElytraPVP.instance!!, Runnable {
            val spawn = Services.regionService.get(spawnRegionName) ?: return@Runnable
            val currentPotato = currentPotatoUuid?.let { Bukkit.getPlayer(it) }

            if (currentPotato == null || !currentPotato.isOnline || spawn.contains(currentPotato.location)) {
                if (currentPotato != null) {
                    removePotatoEffect(currentPotato)
                    if (spawn.contains(currentPotato.location)) {
                        MessageUtils.sendMessage(currentPotato, "&fYou lost the potato because you entered spawn!")
                    }
                }

                val newPotato = findRandomPlayerNotInSpawn()
                if (newPotato != null) {
                    if (!firstAssignmentMade) {
                        MessageUtils.sendAnnouncement("&6${newPotato.name} &fis the first &6&lHOT POTATO&f!")
                        firstAssignmentMade = true
                    }
                    assignPotato(newPotato)
                } else {
                    currentPotatoUuid = null
                }
                return@Runnable
            }

            if (!currentPotato.hasPotionEffect(PotionEffectType.GLOWING)) {
                currentPotato.addPotionEffect(GLOW_EFFECT)
            }

            Services.currencyService.giveGold(currentPotato, 1, "Hot Potato Reward")
            currentPotato.playSound(currentPotato.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 2f)

        }, 0L, 20L)
    }

    override fun deactivate() {
        isActive = false
        logicTask?.cancel()
        logicTask = null

        currentPotatoUuid?.let { Bukkit.getPlayer(it) }?.let { removePotatoEffect(it) }
        currentPotatoUuid = null

        EntityDamageByEntityEvent.getHandlerList().unregister(listener)
        PlayerDeathEvent.getHandlerList().unregister(listener)
        PlayerQuitEvent.getHandlerList().unregister(listener)
    }

    private fun assignPotato(player: Player) {
        currentPotatoUuid?.let { Bukkit.getPlayer(it) }?.let { removePotatoEffect(it) }
        currentPotatoUuid = player.uniqueId
        player.addPotionEffect(GLOW_EFFECT)
        SoundUtils.playSuccess(player)
    }

    private fun removePotatoEffect(player: Player) {
        player.removePotionEffect(PotionEffectType.GLOWING)
    }

    private fun findRandomPlayerNotInSpawn(): Player? {
        val spawn = Services.regionService.get(spawnRegionName) ?: return null
        return Bukkit.getOnlinePlayers()
            .filter { !spawn.contains(it.location) && !CitizensAPI.getNPCRegistry().isNPC(it) }
            .randomOrNull()
    }
}