package me.roustytousty.elytrapvp.data

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object CacheConfig {

    private var file: File = File("")
    private var config: YamlConfiguration = YamlConfiguration()

    fun load(){
        file = File(ElytraPVP.dataFolderDir.path, "cached.yml")

        if (!file.exists()){
            ElytraPVP.instance?.saveResource("cached.yml", false)
            ElytraPVP.instance?.logger?.warning("CACHED NOT FOUND")
        }

        config = YamlConfiguration()
        //config.options().parseComments(true)

        try{
            config.save(file)
            config.load(file)
            config.set("works", true)
            ElytraPVP.instance?.logger?.info("Cached Config Setup Status : ${config.get("works")}")
            ElytraPVP.instance?.logger?.info("${config.get("cached.TTortel.discordId")}")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun reload(){
        file = File(ElytraPVP.dataFolderDir.path, "cached.yml")

        if (!file.exists()){
            ElytraPVP.instance?.saveResource("cached.yml", false)
            ElytraPVP.instance?.logger?.warning("CACHED NOT FOUND")
        }

        config = YamlConfiguration()

        try{
            config.load(file)
            config.set("works", true)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    fun save(){
        try{
            config.save(file)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun getConfig(): YamlConfiguration{
        return config
    }
    fun set(key: String, value: Any?){
        config.set(key, value)
    }
    fun get(key : String): Any? {
        return config.get(key)
    }
    fun getplrVal(plr : Player,key : String): Any?{
        return config.get("cached.${plr.name}.${key}")
    }
    fun setplrVal(plr: OfflinePlayer, key: String, value: Any?): Any?{
        return config.set("cached.${plr.name}.${key}", value)
    }
//    fun getplrBal(plr: OfflinePlayer): Int{
//        return config.getInt("cached.${plr.name}.balance")
//    }
//    fun getplrGameVals(plr : Player, game : String) : List<Map<*, *>> {
//        return config.getMapList("cached.${plr.name}.${game}")
//    }
//
//
//    fun getall(plr : Player){
//        plr.sendMessage("${config.getStringList("cached")}")
//        for (key in config.getStringList("cached")){
//            plr.sendMessage(key)
//        }
//    }
}