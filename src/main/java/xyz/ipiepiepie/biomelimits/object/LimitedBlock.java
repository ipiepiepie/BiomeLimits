package xyz.ipiepiepie.biomelimits.object;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import xyz.ipiepiepie.biomelimits.config.MessagesConfig;
import xyz.ipiepiepie.biomelimits.util.Message;

import java.util.ArrayList;
import java.util.List;

import static xyz.ipiepiepie.biomelimits.config.MessagesConfig.MSG_LIMITED_DESTROY;
import static xyz.ipiepiepie.biomelimits.config.MessagesConfig.MSG_LIMITED_PLACE;

/**
 * Represents {@link Material Block}, limited by {@link Biome Biomes}.
 * <p>
 * Limited {@link Material Block} can have {@link #getWhitelistedBiomes() whitelisted Biomes}, or {@link #getBlacklistedBiomes() blaclisted Biomes}.
 *
 * @see Biome
 */
public class LimitedBlock {
    private final String name;
    private final Material material;
    // lists //
    private final List<Biome> whitelistedBiomes = new ArrayList<>();
    private final List<Biome> blacklistedBiomes = new ArrayList<>();
    // modifiers //
    private final boolean place;
    private final boolean destroy;
    
    
    /**
     * Constructor for new instances.
     */
    public LimitedBlock(String name, Material material, List<Biome> whitelistedBiomes, List<Biome> blacklistedBiomes, boolean place, boolean destroy) {
        this.name = name;
        this.material = material;
        this.whitelistedBiomes.addAll(whitelistedBiomes);
        this.blacklistedBiomes.addAll(blacklistedBiomes);
        this.place = place;
        this.destroy = destroy;
    }
    
    /*================================================================================================*/
    
    /**
     * Get name of current limited block.
     *
     * @return name of block
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get {@link Material} of limited block.
     *
     * @return block {@link Material}
     */
    public Material getMaterial() {
        return material;
    }
    
    /// LIMITED BIOMES LISTS ///
    
    /**
     * Check if {@link Biome} is limited for current block
     *
     * @param biome biome to check
     * @return {@code true} if {@link Biome} is limited, otherwise {@code false}
     */
    public boolean isLimitedBiome(Biome biome) {
        // if whitelist presents, work with it
        if (!getWhitelistedBiomes().isEmpty())
            return !getWhitelistedBiomes().contains(biome);
        
        return getBlacklistedBiomes().contains(biome);
    }
    
    /**
     * Get {@link List} of whitelisted {@link Biome Biomes}.
     * <p>
     * Limitations don't apply to whitelisted {@link Biome Biomes}.
     *
     * @return whitelisted {@link Biome Biomes}
     */
    public List<Biome> getWhitelistedBiomes() {
        return whitelistedBiomes;
    }
    
    /**
     * Get {@link List} of blacklisted {@link Biome Biomes}.
     * <p>
     * Limitations will be applied in blacklisted {@link Biome Biomes}.
     * <p>
     * <b>[!]</b> Note, that blacklist won't work if current block has any {@link #getWhitelistedBiomes() Whitelisted Biomes}.
     *
     * @return blacklisted {@link Biome Biomes}
     */
    public List<Biome> getBlacklistedBiomes() {
        return blacklistedBiomes;
    }
    
    /// LIMITATIONS ///
    
    /**
     * Check if current limited block can be destroyed in blacklisted/non-whitelisted {@link Biome Biomes}.
     *
     * @return {@code true} if can, otherwise {@code false}
     */
    public boolean canBeDestroyed() {
        return destroy;
    }
    
    /**
     * Get destroy {@link Message}.
     * <p>
     * {@link Message} may be unique in some cases.
     *
     * @return Unique destroy {@link Message} if exists, otherwise {@link MessagesConfig#MSG_LIMITED_DESTROY default destroy message}
     */
    public Message getDestroyMessage() {
        String path = "blocks." + getName() + ".msg_limited_destroy"; // path to unique destroy message
        
        return MessagesConfig.getConfig().contains(path) ? new Message(path) : new Message(MSG_LIMITED_DESTROY);
    }
    
    /**
     * Check if current limited block can be placed in blacklisted/non-whitelisted {@link Biome Biomes}.
     *
     * @return {@code true} if can, otherwise {@code false}
     */
    public boolean canBePlaced() {
        return place;
    }
    
    /**
     * Get place {@link Message}.
     * <p>
     * {@link Message} may be unique in some cases.
     *
     * @return Unique place {@link Message} if exists, otherwise {@link MessagesConfig#MSG_LIMITED_PLACE default place message}
     */
    public Message getPlaceMessage() {
        String path = "blocks." + getName() + ".msg_limited_place"; // path to unique place message
        
        return MessagesConfig.getConfig().contains(path) ? new Message(path) : new Message(MSG_LIMITED_PLACE);
    }
    
    /*================================================================================================*/
    
}
