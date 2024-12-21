package me.roustytousty.elytrapvp.data

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object RegionConfig {
    private var file: File = File("")
    private var config: YamlConfiguration = YamlConfiguration()

    fun load() {
        file = File(ElytraPVP.dataFolderDir, "regionconfig.yml")

        if (!file.exists()) {
            ElytraPVP.instance?.saveResource("regionconfig.yml", false)
        }

        try {
            config = YamlConfiguration.loadConfiguration(file)
            ElytraPVP.instance?.logger?.info("Upgrade config loaded successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            ElytraPVP.instance?.logger?.warning("Failed to load upgrade config!")
        }
    }

    fun save(){
        try{
            config.save(file)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getConfig(): YamlConfiguration {
        return config
    }

    fun getRegionPositions(regionName: String): Pair<Location, Location>? {
        val regionSection = config.getConfigurationSection("regions.$regionName") ?: return null

        val worldName = regionSection.getString("pos1.world") ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val pos1 = Location(
            world,
            regionSection.getDouble("pos1.x"),
            regionSection.getDouble("pos1.y"),
            regionSection.getDouble("pos1.z")
        )

        val pos2 = Location(
            world,
            regionSection.getDouble("pos2.x"),
            regionSection.getDouble("pos2.y"),
            regionSection.getDouble("pos2.z")
        )

        return Pair(pos1, pos2)
    }
}