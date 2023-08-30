package xyz.ipiepiepie.biomelimits.object;

import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of {@link Biome Biomes}.
 */
public class BiomeGroup {
    private final String name;
    private final List<Biome> biomes = new ArrayList<>(); // biomes in group
    
    
    /**
     * Constructor for new instances.
     */
    public BiomeGroup(String name, List<Biome> biomes) {
        this.name = name;
        this.biomes.addAll(biomes);
    }
    
    /**
     * Get group name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get {@link Biome Biomes} in current Biome Group.
     */
    public List<Biome> getBiomes() {
        return biomes;
    }
    
}
