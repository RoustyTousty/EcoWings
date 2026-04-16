package me.roustytousty.elytrapvp

import me.roustytousty.elytrapvp.commands.GuiCommands
import me.roustytousty.elytrapvp.commands.TestCommand
import me.roustytousty.elytrapvp.commands.playercommands.*
import me.roustytousty.elytrapvp.commands.staffcommands.*
import me.roustytousty.elytrapvp.commands.staffcommands.guide.*
import me.roustytousty.elytrapvp.data.configs.RegionConfig
import me.roustytousty.elytrapvp.data.configs.UpgradeConfig
import me.roustytousty.elytrapvp.data.api.MongoManager
import me.roustytousty.elytrapvp.data.configs.ShopConfig
import me.roustytousty.elytrapvp.gui.cosmetics.ConfirmCosmeticPurchaseMenu
import me.roustytousty.elytrapvp.gui.cosmetics.CosmeticMenu
import me.roustytousty.elytrapvp.gui.cosmetics.CosmeticSelectMenu
import me.roustytousty.elytrapvp.gui.events.EventsMenu
import me.roustytousty.elytrapvp.gui.events.WarTableMenu
import me.roustytousty.elytrapvp.gui.perks.ConfirmPerkPurchaseMenu
import me.roustytousty.elytrapvp.gui.perks.PerkSelectMenu
import me.roustytousty.elytrapvp.gui.perks.PerksMenu
import me.roustytousty.elytrapvp.gui.rebirth.RebirthMenu
import me.roustytousty.elytrapvp.gui.shops.*
import me.roustytousty.elytrapvp.gui.stats.LeaderboardMenu
import me.roustytousty.elytrapvp.gui.stats.LeaderboardSelectMenu
import me.roustytousty.elytrapvp.gui.stats.PlayerStatsMenu
import me.roustytousty.elytrapvp.gui.stats.StatsMenu
import me.roustytousty.elytrapvp.gui.upgrade.ConfirmUpgradeMenu
import me.roustytousty.elytrapvp.gui.upgrade.UpgradeMenu
import me.roustytousty.elytrapvp.listeners.*
import me.roustytousty.elytrapvp.listeners.interactables.OnCosmeticBlockClick
import me.roustytousty.elytrapvp.listeners.items.consumables.RegenApple
import me.roustytousty.elytrapvp.listeners.items.consumables.SpeedFeather
import me.roustytousty.elytrapvp.listeners.items.utility.Dusty
import me.roustytousty.elytrapvp.listeners.items.utility.Explosive
import me.roustytousty.elytrapvp.listeners.items.utility.Normalizer
import me.roustytousty.elytrapvp.listeners.perks.PerkListener
import me.roustytousty.elytrapvp.services.Services
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ElytraPVP : JavaPlugin() {

    private val pluginmanager = Bukkit.getPluginManager()

    companion object {
        var instance: ElytraPVP? = null
            private set
        var dataFolderDir: File = File("")
            private set
    }

    override fun onEnable() {
        instance = this
        dataFolderDir = dataFolder

        MongoManager.connect("mongodb+srv://roustytousty:JIEOjRyzV0XxVotF@roustytoustydb.nkqhd.mongodb.net/?retryWrites=true&w=majority&appName=RoustyToustyDB")

        setupConfigsOnEnable()

        Services.init(instance!!)

        registerEvents()
        registerCommands()

        logger.info("Plugin Setup!")
    }

    override fun onDisable() {
        Services.goldSpawnService.stopSpawner()
        Services.playerService.saveAndUnloadAllPlayerData()
        Services.eventService.forceStopActiveEvent()
        Services.eventService.saveAllEventData()

        MongoManager.close()
        logger.info("Disabled Plugin!")
    }

    private fun registerEvents(){
        // Special items
        pluginmanager.registerEvents(Dusty(), this)
        pluginmanager.registerEvents(Normalizer(), this)
        pluginmanager.registerEvents(RegenApple(), this)
        pluginmanager.registerEvents(SpeedFeather(), this)
        pluginmanager.registerEvents(Explosive(this), this)

        // Listeners
        pluginmanager.registerEvents(OnPlayerJoin(), this)
        pluginmanager.registerEvents(OnPlayerQuit(), this)
        pluginmanager.registerEvents(OnBlockPlace(), this)
        pluginmanager.registerEvents(OnBlockBreak(), this)
        pluginmanager.registerEvents(OnPlayerMove(), this)
        pluginmanager.registerEvents(OnPlayerRespawn(), this)
        pluginmanager.registerEvents(OnPlayerPostRespawn(), this)
        pluginmanager.registerEvents(OnFoodLevelChange(), this)
        pluginmanager.registerEvents(OnInventoryClose(), this)
        pluginmanager.registerEvents(OnPlayerCraft(), this)
        pluginmanager.registerEvents(OnPlayerDeath(), this)
        pluginmanager.registerEvents(OnPlayerDamage(), this)
        pluginmanager.registerEvents(OnPlayerDrop(), this)
        pluginmanager.registerEvents(OnInventoryClick(), this)
        pluginmanager.registerEvents(OnPlayerInteractEntity(), this)
        pluginmanager.registerEvents(OnPlayerInteract(), this)
        pluginmanager.registerEvents(OnCosmeticBlockClick(), this)
        pluginmanager.registerEvents(OnEntityExplode(), this)
        pluginmanager.registerEvents(OnAsyncPlayerChat(), this)
        pluginmanager.registerEvents(OnPlayerPreLogin(), this)

        pluginmanager.registerEvents(PerkListener(), this)

        // GUI
        pluginmanager.registerEvents(ShopMenu(), this)
        pluginmanager.registerEvents(CategoryShopMenu(), this)
        pluginmanager.registerEvents(UpgradeMenu(), this)
        pluginmanager.registerEvents(ConfirmUpgradeMenu(), this)
        pluginmanager.registerEvents(StatsMenu(), this)
        pluginmanager.registerEvents(PlayerStatsMenu(), this)
        pluginmanager.registerEvents(LeaderboardSelectMenu(), this)
        pluginmanager.registerEvents(LeaderboardMenu(), this)
        pluginmanager.registerEvents(PerksMenu(), this)
        pluginmanager.registerEvents(PerkSelectMenu(), this)
        pluginmanager.registerEvents(ConfirmPerkPurchaseMenu(), this)
        pluginmanager.registerEvents(EventsMenu(), this)
        pluginmanager.registerEvents(WarTableMenu(), this)
        pluginmanager.registerEvents(RebirthMenu(), this)
        pluginmanager.registerEvents(CosmeticMenu(), this)
        pluginmanager.registerEvents(CosmeticSelectMenu(), this)
        pluginmanager.registerEvents(ConfirmCosmeticPurchaseMenu(), this)
    }

    private fun registerCommands(){

        getCommand("test")?.setExecutor(TestCommand())

        // Commands (gui)
        getCommand("shopmenu")?.setExecutor(GuiCommands())
        getCommand("upgrademenu")?.setExecutor(GuiCommands())
        getCommand("statsmenu")?.setExecutor(GuiCommands())
        getCommand("perksmenu")?.setExecutor(GuiCommands())
        getCommand("eventsmenu")?.setExecutor(GuiCommands())
        getCommand("rebirthmenu")?.setExecutor(GuiCommands())
        getCommand("cosmeticmenu")?.setExecutor(GuiCommands())

        // Commands
        getCommand("gold")?.setExecutor(GoldCommand())
        getCommand("stats")?.setExecutor(StatsCommand())
        getCommand("discord")?.setExecutor(DiscordCommand())
        getCommand("spawn")?.setExecutor(SpawnCommand())
        getCommand("ip")?.setExecutor(IpCommand())
        getCommand("link")?.setExecutor(LinkCommand())
        getCommand("unlink")?.setExecutor(UnlinkCommand())
        getCommand("afk")?.setExecutor(AfkCommand())

        // Staff commands
        getCommand("reloadupgradeconfig")?.setExecutor(ReloadUpgradeConfigCommand())
        getCommand("reloadshopconfig")?.setExecutor(ReloadShopConfigCommand())
        getCommand("reloadregionconfig")?.setExecutor(ReloadRegionConfigCommand())
        getCommand("feed")?.setExecutor(FeedCommand())
        getCommand("buildmode")?.setExecutor(BuildModeCommand())
        getCommand("setgold")?.setExecutor(SetGoldCommand())
        getCommand("addgold")?.setExecutor(AddGoldCommand())
        getCommand("setkitlevel")?.setExecutor(SetKitLevelCommand())
        getCommand("eventactivate")?.setExecutor(EventActivateCommand())
        getCommand("mapregionreset")?.setExecutor(MapRegionResetCommand())
        getCommand("maptimerreset")?.setExecutor(MapTimerResetCommand())

        getCommand("clearchat")?.setExecutor(ClearChatCommand())
        getCommand("mutechat")?.setExecutor(MuteChatCommand())
        getCommand("unmutechat")?.setExecutor(UnmuteChatCommand())
        getCommand("mute")?.setExecutor(MuteCommand())
        getCommand("unmute")?.setExecutor(UnmuteCommand())
        getCommand("kick")?.setExecutor(KickCommand())
        getCommand("ban")?.setExecutor(BanCommand())
        getCommand("unban")?.setExecutor(UnbanCommand())
        getCommand("punishmenthistory")?.setExecutor(PunishmentHistoryCommand())
    }

    private fun setupConfigsOnEnable() {
        ShopConfig.load()
        UpgradeConfig.load()
        RegionConfig.load()

        logger.info("Configs Setup!")
    }
}