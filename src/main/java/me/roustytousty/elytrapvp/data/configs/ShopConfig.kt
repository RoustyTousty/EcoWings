package me.roustytousty.elytrapvp.data.configs

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ShopConfig {
    private var file: File = File("")
    private var config: YamlConfiguration = YamlConfiguration()

    fun load() {
        file = File(ElytraPVP.dataFolderDir, "shopconfig.yml")

        if (!file.exists()) {
            ElytraPVP.instance?.saveResource("shopconfig.yml", false)
        }

        try {
            config = YamlConfiguration.loadConfiguration(file)
            ElytraPVP.instance?.logger?.info("Shop config loaded successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            ElytraPVP.instance?.logger?.warning("Failed to load shop config!")
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