package me.roustytousty.elytrapvp.listeners.items

import me.roustytousty.elytrapvp.services.Services
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Explosive(
    private val plugin: JavaPlugin
) : Listener {

    private val regionService = Services.regionService
    private val combatService = Services.combatService

    private val TNT_OWNER_KEY = "tnt_owner"

    private val blockWhitelist = setOf(
        Material.WHITE_WOOL,
        Material.LIGHT_GRAY_WOOL,
        Material.OAK_PLANKS,
        Material.STONE_BRICKS,
        Material.DEEPSLATE_BRICKS
    )

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val location = block.location
        val world = block.world

        if (block.type != Material.TNT) return

        val isInSpawn = regionService.isInRegion(location, "spawnRegion")
        val isInBuildBufferRegion = regionService.isInRegion(location, "buildBufferRegion")
        if (isInSpawn || isInBuildBufferRegion) {
            event.isCancelled = true
            return
        }

        event.block.type = Material.AIR

        val tnt = world.spawnEntity(
            Location(world, location.x + 0.5, location.y + 0.5, location.z + 0.5),
            EntityType.PRIMED_TNT
        ) as TNTPrimed

        tnt.fuseTicks = 50

        tnt.setMetadata(
            TNT_OWNER_KEY,
            FixedMetadataValue(plugin, player.uniqueId.toString())
        )

        world.playSound(location, Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f)
    }

    @EventHandler
    fun onExplode(event: EntityExplodeEvent) {
        val tnt = event.entity as? TNTPrimed ?: return

        if (!tnt.hasMetadata(TNT_OWNER_KEY)) return

        val ownerUUID = tnt.getMetadata(TNT_OWNER_KEY)[0].asString()
        val owner = Bukkit.getPlayer(UUID.fromString(ownerUUID)) ?: return

        val nearbyEntities = tnt.getNearbyEntities(5.0, 5.0, 5.0)

        for (entity in nearbyEntities) {
            val victim = entity as? Player ?: continue
            if (victim == owner) continue

            val victimInSpawn = regionService.isInRegion(victim.location, "spawnRegion")
            if (victimInSpawn) continue

            combatService.tag(victim, owner)
        }

        event.blockList().removeIf { block ->
            block.type !in blockWhitelist
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return

        if (event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
        ) return

        val inSpawn = regionService.isInRegion(player.location, "spawnRegion")
        if (inSpawn) {
            event.isCancelled = true
        }
    }
}