package xyz.ipiepiepie.biomelimits.config;

import com.cryptomorin.xseries.XBiome;

import io.github.townyadvanced.commentedconfiguration.CommentedConfiguration;
import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.Settings;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import xyz.ipiepiepie.biomelimits.BiomeLimits;
import xyz.ipiepiepie.biomelimits.LimitsLogger;
import xyz.ipiepiepie.biomelimits.object.LimitedBlock;
import xyz.ipiepiepie.biomelimits.util.SimpleBiomes;

import java.io.File;
import java.util.*;

/**
 * Config to create {@link LimitedBlock Limited Blocks}.
 *
 * @see LimitedBlock
 */
public class BlocksConfig {
    private static CommentedConfiguration config;
    
    /**
     * Load current config.
     * <p>
     * It'll {@link #initTemplateNodes() create Template Nodes} if config isn't exists.
     */
    public static void load() {
        File configFile = new File(BiomeLimits.getInstance().getDataFolder(), "blocks.yml");
        boolean init = !configFile.exists(); // check if it's config creation (used to load limited block template on creation)
        
        // creates init nodes if config file not exists
        Settings settings = new Settings(configFile.toPath(), BiomeLimits.getInstance(), init ? initTemplateNodes() : new ArrayList<>());
        config = settings.getConfig();
        
        // try to load config
        if (!settings.load()) {
            LimitsLogger.err("&8(Config)&c Can't load &7blocks.yml&c config!");
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
        LimitsLogger.log("&8(Config)&r Loaded &7blocks.yml&r config!");
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
                "# |                               Config of Biome limited Blocks                              | #",
                "# +-------------------------------------------------------------------------------------------+ #",
                "#                                                                                               #",
                "# In this config you can manage limited blocks, add or remove them.                             #",
                "#                                                                                               #",
                "# Each block has its unique ID, represented by Material. (block item)                           #",
                "# Full list of available materials you can see on link below.                                   #",
                "# - https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html                           #",
                "#                                                                                               #",
                "# [!] NOTE! Be sure to use BLOCK Material, instead of ITEM material. (especially with crops)    #",
                "# For example, in Bukkit API there is two CARROT materials: 'CARROT' and 'CARROTS'              #",
                "# 'CARROT' Material represents Carrot item, and 'CARROTS' is Carrot Block.                      #",
                "#                                                                                               #",
                "# [!] Some of the block limitation parameters can be removed, and it won't broke plugin.        #",
                "# These fields marked as 'removable' in example block limitation node below.                    #",
                "#                                                                                               #",
                "#################################################################################################",
                "",
                "#################################################################################################",
                "#                                                                                               #",
                "# You can use my simplified biomes instead of default minecraft biomes.                         #",
                "#                                                                                               #",
                "# Simplified biomes include various biomes combined on the same basis.                          #",
                "# For example, simplified biome S_TAIGA combines biomes TAIGA, TAIGA_HILLS and TAIGA_MOUNTAINS. #",
                "#                                                                                               #",
                "# List of simplified biomes, that you can use:                                                  #",
                "# · S_FOREST (FOREST, WOODED_HILLS, FLOWER_FOREST)                                              #",
                "# · S_BIRCH_FOREST (all birch and tall birch biomes)                                            #",
                "# · S_DARK_FOREST (all dark forest biomes)                                                      #",
                "# · S_JUNGLE (all jungle biomes except bamboo biomes)                                           #",
                "# · S_SWAMP (all swamp biomes)                                                                  #",
                "# · S_PLAINS (PLAINS and SUNFLOWER_PLAINS biomes)                                               #",
                "# · S_DESERT (all desert biomes)                                                                #",
                "# · S_BADLANDS (all badlands biomes)                                                            #",
                "# · S_TUNDRA (all tundra and snowy taiga biomes)                                                #",
                "# · S_TAIGA (all taiga biomes)                                                                  #",
                "# · S_SAVANNA (all savanna biomes)                                                              #",
                "# · S_MOUNTAINS (all mountains biomes)                                                          #",
                "# · S_OCEAN (all ocean biomes)                                                                  #",
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
                .builder("carrots", Object.class)
                .comment("# example of biome limited carrot, with explanation in comments. This template can be removed/edited.")
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("carrots.whitelist", List.class)
                .comment("# whitelisted biomes  (removable)")
                .comment("# [?] default implementation means, that carrot can be placed/destroyed only in plains biomes and flower forest")
                .defaultValue(List.of("S_PLAINS", "FLOWER_FOREST"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("carrots.blacklist", List.class)
                .comment("# blacklisted biomes  (removable)")
                .comment("# [?] default implementation means, that carrot can't be placed/destroyed in all taiga, mountains and ocean biomes")
                .comment("# [!] note, that blacklist won't work, since we are already using whitelist in current implementation!")
                .defaultValue(List.of("S_TAIGA", "S_MOUNTAINS"))
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("carrots.destroy", Boolean.class)
                .comment("")
                .comment("# determines if block can be destroyed in limited biomes  (removable)")
                .comment("# [?] default implementation means, that carrot can be destroyed (harvested) in blacklisted/non-whitelisted biomes")
                .defaultValue(true)
                .build()
        );
        
        nodes.add(SimpleNode
                .builder("carrots.place", Boolean.class)
                .comment("# determines if block can be placed in limited biomes  (removable)")
                .comment("# [?] default implementation means, that carrot can't be placed (planted) in blacklisted/non-whitelisted biomes")
                .defaultValue(false)
                .build()
        );
        
        return nodes;
    }
    
    /*================================================================================================*/
    
    /// MATERIAL ///
    
    /**
     * Get {@link Material} of {@link LimitedBlock Limited Block}
     *
     * @param block block <i>(Material name)</i> to get {@link Material} from.
     * @return {@link Material} if exists, otherwise {@code null}
     */
    public static Material getMaterial(String block) {
        return Material.getMaterial(block.toUpperCase(Locale.ENGLISH));
    }
    
    /// BLACKLISTED BIOMES ///
    
    /**
     * Get {@link List} of blacklisted {@link Biome Biomes} for current limited block.
     *
     * @param block {@link LimitedBlock Limited Block} name
     * @return blacklisted {@link Biome Biomes}
     *
     * @see #getBiomes(String, List)
     */
    public static List<Biome> getBlacklistedBiomes(String block) {
        return getBiomes(block, getConfig().getStringList(block + ".blacklist"));
    }
    
    /// WHITELISTED BIOMES ///
    
    /**
     * Get {@link List} of whitelisted {@link Biome Biomes} for current limited block.
     *
     * @param block {@link LimitedBlock Limited Block} name
     * @return whitelisted {@link Biome Biomes}
     *
     * @see #getBiomes(String, List)
     */
    public static List<Biome> getWhitelistedBiomes(String block) {
        return getBiomes(block, getConfig().getStringList(block + ".whitelist"));
    }
    
    // BIOME LOADER //
    
    /**
     * Load {@link List} of {@link Biome Biomes} from its names.
     * <p>
     * First of all tries to parse {@link SimpleBiomes}, then {@link XBiome}. <br>
     * Sends warning, if can't process {@link Biome}.
     *
     * @param block {@link LimitedBlock Limited Block} name for warning messages
     * @param names names of {@link Biome Biomes}
     * @return {@link List} of {@link Biome Biomes}
     */
    private static List<Biome> getBiomes(String block, List<String> names) {
        List<Biome> biomes = new ArrayList<>();
        
        // iterate over biome names
        for (String name : names) {
            // simple parsing for simple biome, because it's simple!
            if (SimpleBiomes.isSimplifiedBiome(name)) {
                biomes.addAll(SimpleBiomes.valueOf(name).getBiomes());
            } else {  // otherwise dive into biome existence checks >_<
                XBiome biome = XBiome.matchXBiome(name).orElse(null);
                
                if (biome != null)  // add biome if exists
                    biomes.add(biome.getBiome());
                else  // warn to console, if biome doesn't exist on current version
                    LimitsLogger.warn("Can't load biome '%s' for limited block '%s'", name, block);
            }
        }
        
        return biomes;
    }
    
    /// LIMITATIONS ///
    
    /**
     * Get if {@link LimitedBlock Limited Block} can be placed in blacklisted/non-whitelisted {@link Biome Biomes}.
     *
     * @param block {@link LimitedBlock Limited Block} name
     * @return {@code true} if {@link LimitedBlock Limited Block} can be placed without limitations, or {@code false} if not
     */
    public static boolean canBePlaced(String block) {
        return getConfig().getBoolean(block + ".place", true);
    }
    
    /**
     * Get if {@link LimitedBlock Limited Block} can be destroyed in blacklisted/non-whitelisted {@link Biome Biomes}.
     *
     * @param block {@link LimitedBlock Limited Block} name
     * @return {@code true} if {@link LimitedBlock Limited Block} can be destroyed without limitations, or {@code false} if not
     */
    public static boolean canBeDestroyed(String block) {
        return getConfig().getBoolean(block + ".destroy", true);
    }
    
    /*================================================================================================*/
    
}
