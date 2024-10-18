package me.roustytousty.elytrapvp.data

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object UpgradeConfig {
    private var file: File = File("")
    private var config: YamlConfiguration = YamlConfiguration()

    fun load() {
        file = File(ElytraPVP.dataFolderDir, "upgradeconfig.yml")

        if (!file.exists()) {
            ElytraPVP.instance?.saveResource("upgradeconfig.yml", false)
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