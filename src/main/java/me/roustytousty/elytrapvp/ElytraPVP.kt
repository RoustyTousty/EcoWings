package me.roustytousty.elytrapvp

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.commands.GuiCommands
import me.roustytousty.elytrapvp.commands.TestCommand
import me.roustytousty.elytrapvp.commands.playercommands.GoldCommand
import me.roustytousty.elytrapvp.commands.playercommands.StatsCommand
import me.roustytousty.elytrapvp.commands.staffcommands.*
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.gui.events.EventsMenu
import me.roustytousty.elytrapvp.gui.events.WarTableMenu
import me.roustytousty.elytrapvp.gui.perks.PerksMenu
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
import me.roustytousty.elytrapvp.listeners.items.RegenApple
import me.roustytousty.elytrapvp.listeners.items.Dusty
import me.roustytousty.elytrapvp.services.ScoreboardService
import me.roustytousty.elytrapvp.utility.RegionUtils
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
        ScoreboardService().startUpdateTask()
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
        pluginmanager.registerEvents(Dusty(), this)
        pluginmanager.registerEvents(RegenApple(), this)

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
        pluginmanager.registerEvents(OnInventoryClick(), this)
        pluginmanager.registerEvents(OnPlayerInteractEntity(), this)
        pluginmanager.registerEvents(OnWarTableClick(), this)

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
        pluginmanager.registerEvents(PerksMenu(), this)
        pluginmanager.registerEvents(EventsMenu(), this)
        pluginmanager.registerEvents(WarTableMenu(), this)
    }

    private fun registerCommands(){

        getCommand("test")?.setExecutor(TestCommand())

        // Commands
        getCommand("gold")?.setExecutor(GoldCommand())
        getCommand("stats")?.setExecutor(StatsCommand())

        // Commands (gui)
        getCommand("shopmenu")?.setExecutor(GuiCommands())
        getCommand("upgrademenu")?.setExecutor(GuiCommands())
        getCommand("statsmenu")?.setExecutor(GuiCommands())
        getCommand("perksmenu")?.setExecutor(GuiCommands())
        getCommand("eventsmenu")?.setExecutor(GuiCommands())

        // Staff commands
        getCommand("reloadcache")?.setExecutor(ReloadCacheCommand())
        getCommand("reloadupgradeconfig")?.setExecutor(ReloadUpgradeConfigCommand())
        getCommand("feed")?.setExecutor(FeedCommand())
        getCommand("buildmode")?.setExecutor(BuildModeCommand())
        getCommand("setgold")?.setExecutor(SetGoldCommand())
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