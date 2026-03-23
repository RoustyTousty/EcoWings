package me.roustytousty.elytrapvp.data.configs

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
}