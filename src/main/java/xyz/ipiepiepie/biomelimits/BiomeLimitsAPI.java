package xyz.ipiepiepie.biomelimits;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import xyz.ipiepiepie.biomelimits.config.BlocksConfig;
import xyz.ipiepiepie.biomelimits.object.LimitedBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API class with methods to interact {@link BiomeLimits} system.
 * <p>
 * There is a method to {@link #load(boolean) load} plugin's data from configs.
 * <p>
 * This class can be used as a bridge between your plugins and {@link BiomeLimits} system.
 */
public class BiomeLimitsAPI {
    private static BiomeLimitsAPI instance;
    // limited blocks //
    private final Map<Material, LimitedBlock> limitedBlocks = new ConcurrentHashMap<>();
    
    /**
     * Get access to {@link BiomeLimitsAPI API class}
     *
     * @return instance of {@link BiomeLimitsAPI API class}
     */
    public static BiomeLimitsAPI getInstance() {
        return instance;
    }
    
    /*===================================* LIMITED BLOCKS METHODS *===================================*/
    
    /**
     * Get {@link LimitedBlock Limited Block} by its {@link Material}
     *
     * @param material material to get block from
     *
     * @return {@link LimitedBlock Limited Block} if exists, otherwise {@code null}
     */
    public LimitedBlock getLimitedBlock(Material material) {
        return limitedBlocks.get(material);
    }
    
    /**
     * Get {@link LimitedBlock Limited Block} by its name.
     *
     * @param name name of {@link LimitedBlock Limited Block} in config
     * @return {@link LimitedBlock} if exists, otherwise {@code null}
     */
    public LimitedBlock getLimitedBlock(String name) {
        return limitedBlocks.values()
                .stream()
                .filter(block -> block.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all registered {@link LimitedBlock Limited Blocks}.
     *
     * @return {@link List} of all {@link LimitedBlock Limited Blocks}
     */
    public List<LimitedBlock> getLimitedBlocks() {
        return new ArrayList<>(limitedBlocks.values());
    }
    
    /*========================================* LOAD METHODS *========================================*/
    
    /**
     * Load <i>(or reload)</i> plugin.
     * <p>
     * {@link #loadLimitedBlocks() Loads} {@link LimitedBlock Limited Blocks} from {@link BlocksConfig config}
     *
     * @param init defines if it's first load of API <i>(otherwise it'll be reload)</i>
     *
     * @see #loadLimitedBlocks()
     */
    public void load(boolean init) {
        if (init)
            instance = this; // create instance
        
        // track loading time
        long startTime = System.currentTimeMillis();
        
        // load data from configs
        this.loadLimitedBlocks();
        
        // debug
        LimitsLogger.debug("Loading data from configs took %dms", System.currentTimeMillis() - startTime);
    }
    
    /// SUB-LOAD METHODS ///
    
    /**
     * Load {@link LimitedBlock Limited Blocks} from {@link BlocksConfig}.
     * <p>
     * Automatically puts loaded {@link LimitedBlock Limited Blocks} to {@link #limitedBlocks Limited Blocks Map}.
     *
     * @see BlocksConfig
     */
    private void loadLimitedBlocks() {
        // get limited block materials
        Set<String> identifiers = BlocksConfig.getConfig().getKeys(false);
        limitedBlocks.clear(); // clear old limited blocks map
        
        // iterate over limited blocks' IDs and load each limited block
        for (String block : identifiers) {
            Material material = BlocksConfig.getMaterial(block);
            
            // skip if we can't load limited block
            if (material == null) {
                LimitsLogger.warn("Can't load &6%s&e material for limited block!", block);
                continue;
            } else if (!material.isBlock()) {
                LimitsLogger.warn("Material &6%s&e isn't block!", block);
                continue;
            }
            
            // load and save limited block in internal structures
            limitedBlocks.put(material, new LimitedBlock(
                    block,
                    material,
                    BlocksConfig.getWhitelistedBiomes(block),
                    BlocksConfig.getBlacklistedBiomes(block),
                    BlocksConfig.canBePlaced(block),
                    BlocksConfig.canBeDestroyed(block)
            ));
        }
        
        // log to console
        LimitsLogger.log("&8(Loader) &7Loaded &a%d &7Limited Blocks! %s(%d/%d)",
                limitedBlocks.size(),
                limitedBlocks.size() == identifiers.size() ? ChatColor.GREEN : ChatColor.RED,
                limitedBlocks.size(),
                identifiers.size()
        );
    }
    
    /*================================================================================================*/
    
}
