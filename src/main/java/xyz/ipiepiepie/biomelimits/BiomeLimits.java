package xyz.ipiepiepie.biomelimits;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.ipiepiepie.biomelimits.command.BiomeLimitsCommand;
import xyz.ipiepiepie.biomelimits.config.BlocksConfig;
import xyz.ipiepiepie.biomelimits.config.MessagesConfig;
import xyz.ipiepiepie.biomelimits.config.SettingsConfig;
import xyz.ipiepiepie.biomelimits.listener.BlockListener;

/**
 * Entry point of plugin.
 * <p>
 * <b>NOTE!</b> There is no API methods. If you want to use plugin's API, then go to {@link BiomeLimitsAPI} class!
 *
 * @author ipiepiepie
 * @see <a href="https://github.com/ipiepiepie/BiomeLimits">GitHub Repository</a>
 */
public final class BiomeLimits extends JavaPlugin {
    private static BiomeLimits instance; // plugin instance
    
    /**
     * Get access to {@link BiomeLimits} Plugin class.
     *
     * @return {@link BiomeLimits} instance.
     */
    public static BiomeLimits getInstance() {
        return instance;
    }
    
    /*========================================* LOAD METHODS *========================================*/
    
    @Override
    public void onLoad() {
        // load CommandAPI commands and disable annoying logs
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
        
        // register commands (it will be registered as minecraft:commands)
        BiomeLimitsCommand.getCommand().register();
    }
    
    @Override
    public void onEnable() {
        instance = this; // create instance
        long startTime = System.currentTimeMillis(); // track plugin startup time
        
        Bukkit.getLogger().info("=======================  BiomeLimits  =======================");
        
        // load configs
        this.loadConfigs();
        
        // load Biome Limits API class
        new BiomeLimitsAPI().load(true);
        
        // register events
        this.registerEvents();
        
        // log start time to console
        LimitsLogger.log("Plugin enabled in &7%dms", System.currentTimeMillis() - startTime);
        
        Bukkit.getLogger().info("=============================================================");
        LimitsLogger.log("Version &7%s&r - Plugin Enabled!", getDescription().getVersion());
        Bukkit.getLogger().info("=============================================================");
    }
    
    /// SUB-LOAD METHODS ///
    
    /**
     * Loads <i>(or reloads)</i> all configs.
     * <p>
     * Also, tries to create {@link #getDataFolder() Plugin Data Folder} and disables plugin, if creation isn't success.
     *
     * @see SettingsConfig
     * @see BlocksConfig
     * @see MessagesConfig
     */
    public void loadConfigs() {
        long startTime = System.currentTimeMillis();
        
        // try to create plugin folder if not exist
        if (!getDataFolder().exists() && !getDataFolder().mkdir()) {
            LimitsLogger.err("&8(Loader)&c Can't create plugin data folder!");
            LimitsLogger.err("Please, check your filesystem permissions settings");
            
            // disable plugin
            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        // load configs
        SettingsConfig.load();
        BlocksConfig.load();
        MessagesConfig.load();
        
        // debug
        LimitsLogger.debug("Configs load took %dms", System.currentTimeMillis() - startTime);
    }
    
    /**
     * Registers Events
     *
     * @see BlockListener
     */
    private void registerEvents() {
        long startTime = System.currentTimeMillis();
        
        // register events
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        
        // debug
        LimitsLogger.debug("Event register took %dms", System.currentTimeMillis() - startTime);
    }
    
    /*======================================* SHUTDOWN METHODS *======================================*/
    
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("=============================================================");
        
        // disable CommandAPI
        CommandAPI.onDisable();
        
        LimitsLogger.log("Version &7%s&r - Plugin Disabled!", getDescription().getVersion());
        Bukkit.getLogger().info("=============================================================");
    }
    
    /*================================================================================================*/
    
}
