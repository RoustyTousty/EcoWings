package me.roustytousty.elytrapvp

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.commands.GuiCommands
import me.roustytousty.elytrapvp.commands.PlayerCommands
import me.roustytousty.elytrapvp.commands.StaffCommands
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.gui.shops.BlockShopMenu
import me.roustytousty.elytrapvp.gui.shops.ConsumablesShopMenu
import me.roustytousty.elytrapvp.gui.shops.ShopMenu
import me.roustytousty.elytrapvp.gui.shops.UtilityShopMenu
import me.roustytousty.elytrapvp.gui.stats.LeaderboardMenu
import me.roustytousty.elytrapvp.gui.stats.LeaderboardSelectMenu
import me.roustytousty.elytrapvp.gui.stats.PlayerStatsMenu
import me.roustytousty.elytrapvp.gui.stats.StatsMenu
import me.roustytousty.elytrapvp.gui.upgrade.ConfirmUpgradeMenu
import me.roustytousty.elytrapvp.gui.upgrade.UpgradeMenu
import me.roustytousty.elytrapvp.listeners.*
import me.roustytousty.elytrapvp.listeners.consumables.OnRegenApple
import me.roustytousty.elytrapvp.listeners.utility.OnDusty
import me.roustytousty.elytrapvp.utility.RegionUtils
import me.roustytousty.elytrapvp.utility.ScoreboardUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ElytraPVP : JavaPlugin() {
    val pluginmanager = Bukkit.getPluginManager()

    companion object {
        var instance: ElytraPVP? = null
            private set
        var dataFolderDir: File = File("")
            private set
    }

    override fun onEnable() {
        instance = this
        dataFolderDir = dataFolder

        setupConfigsOnEnable()
        registerEvents()
        registerCommands()
        MongoDB.setupDbOnEnable()
        ScoreboardUtils.startUpdatingScoreboard()
        RegionUtils.startRegionResetTask()

        logger.info("Plugin Setup!")
    }

    override fun onDisable() {

        MongoDB.saveAllCache()
        setupConfigsOnDisable()
        MongoDB.setupDbOnDisable()

        logger.info("Disabled Plugin!")
    }

    private fun registerEvents(){
        // Special items
        pluginmanager.registerEvents(OnDusty(), this)
        pluginmanager.registerEvents(OnRegenApple(), this)

        // Listeners
        pluginmanager.registerEvents(OnPlayerJoin(), this)
        pluginmanager.registerEvents(OnPlayerQuit(), this)
        pluginmanager.registerEvents(OnBlockPlace(), this)
        pluginmanager.registerEvents(OnBlockBreak(), this)
        pluginmanager.registerEvents(OnPlayerMove(), this)
        pluginmanager.registerEvents(OnPlayerRespawn(), this)
        pluginmanager.registerEvents(OnFoodLevelChange(), this)
        pluginmanager.registerEvents(OnPlayerCraft(), this)
        pluginmanager.registerEvents(OnPlayerDeath(), this)
        pluginmanager.registerEvents(OnPlayerDamage(), this)
        pluginmanager.registerEvents(OnPlayerDrop(), this)

        // GUI
        pluginmanager.registerEvents(ShopMenu(), this)
        pluginmanager.registerEvents(BlockShopMenu(), this)
        pluginmanager.registerEvents(UtilityShopMenu(), this)
        pluginmanager.registerEvents(ConsumablesShopMenu(), this)
        pluginmanager.registerEvents(UpgradeMenu(), this)
        pluginmanager.registerEvents(ConfirmUpgradeMenu(), this)
        pluginmanager.registerEvents(StatsMenu(), this)
        pluginmanager.registerEvents(PlayerStatsMenu(), this)
        pluginmanager.registerEvents(LeaderboardSelectMenu(), this)
        pluginmanager.registerEvents(LeaderboardMenu(), this)
    }

    private fun registerCommands(){
        // Commands
        getCommand("gold")?.setExecutor(PlayerCommands())

        // Commands (gui)
        getCommand("shop")?.setExecutor(GuiCommands())
        getCommand("upgrade")?.setExecutor(GuiCommands())
        getCommand("stats")?.setExecutor(GuiCommands())

        // Staff commands
        getCommand("reloadcache")?.setExecutor(StaffCommands())
        getCommand("reloadupgradeconfig")?.setExecutor(StaffCommands())
        getCommand("feed")?.setExecutor(StaffCommands())
        getCommand("buildmode")?.setExecutor(StaffCommands())
        getCommand("setgold")?.setExecutor(StaffCommands())
    }

    private fun setupConfigsOnEnable() {
        config.set("plrcount", 0)
        saveConfig()

        CacheConfig.load()
        UpgradeConfig.load()
        RegionConfig.load()

        logger.info("Configs Setup!")
    }
    private fun setupConfigsOnDisable() {
        CacheConfig.save()

        logger.info("Configs Saved! ")
    }
}