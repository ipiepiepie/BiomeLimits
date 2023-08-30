package xyz.ipiepiepie.biomelimits.config;

import com.cryptomorin.xseries.XBiome;
import io.github.townyadvanced.commentedconfiguration.CommentedConfiguration;
import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.Settings;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;

import org.bukkit.Bukkit;

import org.bukkit.block.Biome;
import xyz.ipiepiepie.biomelimits.BiomeLimits;
import xyz.ipiepiepie.biomelimits.LimitsLogger;
import xyz.ipiepiepie.biomelimits.object.BiomeGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Config to create {@link BiomeGroup Biome Groups}.
 *
 * @see BiomeGroup
 */
public class BiomesConfig {
    private static CommentedConfiguration config;
    
    /**
     * Load current config.
     * <p>
     * It'll {@link #initTemplateNodes() create Template Nodes} if config isn't exists.
     */
    public static void load() {
        File configFile = new File(BiomeLimits.getInstance().getDataFolder(), "biomes.yml");
        boolean init = !configFile.exists(); // check if it's config creation (used to load limited block template on creation)
        
        // creates init nodes if config file not exists
        Settings settings = new Settings(configFile.toPath(), BiomeLimits.getInstance(), init ? initTemplateNodes() : new ArrayList<>());
        config = settings.getConfig();
        
        // try to load config
        if (!settings.load()) {
            LimitsLogger.err("&8(Config)&c Can't load &7biomes.yml&c config!");
            LimitsLogger.err("Please, check your filesystem permissions settings");
            
            // disable plugin
            Bukkit.getPluginManager().disablePlugin(BiomeLimits.getInstance());
            return;
        }
        
        // add header to top of config
        if (!getConfig().getKeys(false).isEmpty())
            addHeader(getConfig().getKeys(false).iterator().next());
        
        // save config to file (otherwise we'll have empty file)
        settings.save();
        LimitsLogger.log("&8(Config)&r Loaded &7biomes.yml&r config!");
    }
    
    public static CommentedConfiguration getConfig() {
        return config;
    }
    
    /*================================================================================================*/
    
    /**
     * Generate header for config file.
     *
     * @param path path to first note in {@link CommentedConfiguration}
     */
    private static void addHeader(String path) {
        config.addComment(path,
                "#################################################################################################",
                "# +-------------------------------------------------------------------------------------------+ #",
                "# |                                  Config of Biome Groups                                   | #",
                "# +-------------------------------------------------------------------------------------------+ #",
                "#                                                                                               #",
                "# In this config you can create groups of Biomes.                                               #",
                "#                                                                                               #",
                "# Each group has its unique ID, which will be prefixed by 'GROUP_'.                             #",
                "# It's needed to avoid situations, when you have 'FOREST' biome and 'FOREST' group together.    #",
                "# So, for example, if you have Biome Group named 'FOREST', it's final name                      #",
                "# after plugin loading will be 'GROUP_FOREST'                                                   #",
                "#                                                                                               #",
                "# Full list of available Biomes you can see on link below.                                      #",
                "# - https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/Biome.html                        #",
                "#                                                                                               #",
                "#################################################################################################",
                "",
                ""
        );
    }
    
    /// INIT NODES ///
    
    /**
     * Initialise template nodes.
     * <p>
     * They will be added only on creation of config file.
     */
    private static List<CommentedNode> initTemplateNodes() {
        List<CommentedNode> nodes = new ArrayList<>();
        
        nodes.add(SimpleNode
                .builder("FOREST", List.class)
                .comment("# Auto-generated forest biome group.")
                .comment("# Available via 'GROUP_FOREST'")
                .defaultValue(List.of("FOREST", "WOODED_HILLS", "FLOWER_FOREST"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("BIRCH_FOREST", List.class)
                .comment("")
                .comment("# Auto-generated birch forest biome group.")
                .comment("# Available via 'GROUP_BIRCH_FOREST'")
                .defaultValue(List.of("BIRCH_FOREST", "BIRCH_FOREST_HILLS", "TALL_BIRCH_FOREST", "TALL_BIRCH_HILLS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("DARK_FOREST", List.class)
                .comment("")
                .comment("# Auto-generated dark forest biome group.")
                .comment("# Available via 'GROUP_DARK_FOREST'")
                .defaultValue(List.of("DARK_FOREST", "DARK_FOREST_HILLS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("JUNGLE", List.class)
                .comment("")
                .comment("# Auto-generated jungle biome group.")
                .comment("# Available via 'GROUP_JUNGLE'")
                .defaultValue(List.of("JUNGLE", "JUNGLE_EDGE", "JUNGLE_HILLS", "MODIFIED_JUNGLE", "MODIFIED_JUNGLE_EDGE"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("SWAMP", List.class)
                .comment("")
                .comment("# Auto-generated swamp biome group.")
                .comment("# Available via 'GROUP_SWAMP'")
                .defaultValue(List.of("SWAMP", "SWAMP_HILLS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("PLAINS", List.class)
                .comment("")
                .comment("# Auto-generated plains biome group.")
                .comment("# Available via 'GROUP_PLAINS'")
                .defaultValue(List.of("PLAINS", "SUNFLOWER_PLAINS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("DESERT", List.class)
                .comment("")
                .comment("# Auto-generated desert biome group.")
                .comment("# Available via 'GROUP_DESERT'")
                .defaultValue(List.of("DESERT", "DESERT_HILLS", "DESERT_LAKES"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("BADLANDS", List.class)
                .comment("")
                .comment("# Auto-generated badlands biome group.")
                .comment("# Available via 'GROUP_BADLANDS'")
                .defaultValue(List.of("BADLANDS", "BADLANDS_PLATEAU", "ERODED_BADLANDS", "MODIFIED_BADLANDS_PLATEAU", "WOODED_BADLANDS_PLATEAU", "MODIFIED_WOODED_BADLANDS_PLATEAU"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("TUNDRA", List.class)
                .comment("")
                .comment("# Auto-generated tundra biome group.")
                .comment("# Available via 'GROUP_TUNDRA'")
                .defaultValue(List.of("SNOWY_TUNDRA", "SNOWY_TAIGA", "SNOWY_TAIGA_HILLS", "SNOWY_TAIGA_MOUNTAINS", "SNOWY_BEACH"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("TAIGA", List.class)
                .comment("")
                .comment("# Auto-generated taiga biome group.")
                .comment("# Available via 'GROUP_TAIGA'")
                .defaultValue(List.of("TAIGA", "TAIGA_MOUNTAINS", "TAIGA_HILLS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("SAVANNA", List.class)
                .comment("")
                .comment("# Auto-generated savanna biome group.")
                .comment("# Available via 'GROUP_SAVANNA'")
                .defaultValue(List.of("SAVANNA", "SAVANNA_PLATEAU", "SHATTERED_SAVANNA", "SHATTERED_SAVANNA_PLATEAU"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("MOUNTAINS", List.class)
                .comment("")
                .comment("# Auto-generated mountains biome group.")
                .comment("# Available via 'GROUP_MOUNTAINS'")
                .defaultValue(List.of("MOUNTAINS", "MOUNTAIN_EDGE", "GRAVELLY_MOUNTAINS", "MODIFIED_GRAVELLY_MOUNTAINS", "SNOWY_MOUNTAINS", "WOODED_MOUNTAINS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("OCEAN", List.class)
                .comment("")
                .comment("# Auto-generated ocean biome group.")
                .comment("# Available via 'GROUP_OCEAN'")
                .defaultValue(List.of("OCEAN", "COLD_OCEAN", "DEEP_COLD_OCEAN", "DEEP_FROZEN_OCEAN", "FROZEN_OCEAN", "DEEP_LUKEWARM_OCEAN", "DEEP_WARM_OCEAN", "WARM_OCEAN", "LUKEWARM_OCEAN", "DEEP_OCEAN"))
                .build()
        );
        
        return nodes;
    }
    
    /*================================================================================================*/
    
    /**
     * Get {@link Biome Biomes} in {@link BiomeGroup Biome Group}.
     *
     * @see BiomeGroup
     */
    public static List<Biome> getBiomes(String id) {
        List<String> names = getConfig().getStringList("id"); // names of biomes
        List<Biome> biomes = new ArrayList<>();
        
        // iterate over biome names
        for (String name : names) {
            XBiome biome = XBiome.matchXBiome(name).orElse(null);
            
            // warn if Biome doesn't exist in all versions
            if (biome == null) {
                LimitsLogger.warn("Can't find biome '%s' for group '%s'", name, id);
                continue;
            }
            
            // add biome to map if exists
            if (biome.getBiome() != null)
                biomes.add(biome.getBiome());
        }
        
        return biomes;
    }
    
    /*================================================================================================*/
    
}
