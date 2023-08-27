package xyz.ipiepiepie.biomelimits.config;

import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.Settings;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;
import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;

import org.bukkit.Bukkit;
import xyz.ipiepiepie.biomelimits.BiomeLimits;
import xyz.ipiepiepie.biomelimits.LimitsLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Config to control {@link BiomeLimits} settings.
 */
public class SettingsConfig {
    private static final List<CommentedNode> nodes = new ArrayList<>();
    private static Settings settings;
    
    /**
     * Load current config.
     * <p>
     * It'll refresh all config nodes and commentaries.
     */
    public static void load() {
        File configFile = new File(BiomeLimits.getInstance().getDataFolder(), "settings.yml");
        settings = new Settings(configFile.toPath(), BiomeLimits.getInstance(), nodes);
        
        // try to load config
        if (!settings.load()) {
            LimitsLogger.err("&8(Config)&c Can't load &7settings.yml&c config!");
            LimitsLogger.err("Please, check your filesystem permissions settings");
            
            // disable plugin
            Bukkit.getPluginManager().disablePlugin(BiomeLimits.getInstance());
            return;
        }
        
        // save config to file (otherwise we'll have empty file)
        settings.save();
        LimitsLogger.log("&8(Config)&r Loaded &7settings.yml&r config!");
    }
    
    /*================================================================================================*/
    
    /**
     * Getter for config nodes.
     */
    public static <T> T get(TypedValueNode<T> node) {
        return settings.get(node);
    }
    
    /**
     * Builder to construct config nodes.
     */
    private static <T> TypedValueNode<T> node(TypedValueNode<T> node) {
        nodes.add(node);
        return node;
    }
    
    /*==========================================* CONFIG NODES *======================================*/
    
    private static final TypedValueNode<Object> LIMITED_BLOCK_INFO = node(SimpleNode
            .builder("limited-block-info", Object.class)
            .comment("#############################################################") // not good, but Towny uses the same way
            .comment("# +-------------------------------------------------------+ #")
            .comment("# |                    Settings config                    | #")
            .comment("# +-------------------------------------------------------+ #")
            .comment("#                                                           #")
            .comment("# Here you can find global plugin settings.                 #")
            .comment("#                                                           #")
            .comment("#############################################################")
            .comment("")
            .comment("")
            .comment("# limited block info menu settings")
            .build());
    
    public static final TypedValueNode<Integer> LIMITED_BLOCK_INFO_BIOMES_PER_LINE = node(SimpleNode
            .builder("limited-block-info.biomes-per-line", Integer.class)
            .comment("# how much Biomes would be displayed on one line in Limited Block Info menu")
            .comment("# (Default: 4)")
            .defaultValue(4)
            .build());
    
    public static final TypedValueNode<Boolean> DEBUG = node(SimpleNode
            .builder("debug", Boolean.class)
            .comment("")
            .comment("# toggles debug mode")
            .comment("# (Default: false)")
            .defaultValue(false)
            .build());
    
    /*================================================================================================*/
}
