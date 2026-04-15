package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.event.EventInterface
import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask

class VoidlessEvent : EventInterface {
    override val name = "Voidless"
    override val description = "No void, no problem."
    override val displayMaterial = Material.SCULK
    override val cost = 400
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    private val regionName = "voidlessBlock"

    override fun activate() {
        isActive = true
        updateFloor(Material.WHITE_STAINED_GLASS)
    }

    override fun deactivate() {
        isActive = false
        updateFloor(Material.AIR)
    }

    private fun updateFloor(material: Material) {
        val region = Services.regionService.get(regionName) ?: return
        val minY = region.getMinY()

        region.forEachBlock { block ->
            if (block.y == minY) {
                block.type = material
            }
        }
    }
}