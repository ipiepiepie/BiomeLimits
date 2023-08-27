package xyz.ipiepiepie.biomelimits.util;

import org.bukkit.block.Biome;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents global {@link Biome Biomes}, such as Taiga, Desert, and others...
 * <p>
 * Every simplified biome contains several {@link Biome Minecraft Sub-Biomes}, like hills, plains and others.
 * <p>
 * Used to simplify {@link Biome} matching from config.
 *
 * @see Biome
 */
@SuppressWarnings("unused")
public enum SimpleBiomes {
    // TODO add XBiome biomes support
    S_FOREST(List.of(Biome.FOREST, Biome.WOODED_HILLS, Biome.FLOWER_FOREST)),
    S_BIRCH_FOREST(List.of(Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.TALL_BIRCH_FOREST, Biome.TALL_BIRCH_HILLS)),
    S_DARK_FOREST(List.of(Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS)),
    S_JUNGLE(List.of(Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE)),
    S_SWAMP(List.of(Biome.SWAMP, Biome.SWAMP_HILLS)),
    S_PLAINS(List.of(Biome.PLAINS, Biome.SUNFLOWER_PLAINS)),
    S_DESERT(List.of(Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES)),
    S_BADLANDS(List.of(Biome.BADLANDS, Biome.BADLANDS_PLATEAU, Biome.ERODED_BADLANDS, Biome.MODIFIED_BADLANDS_PLATEAU, Biome.WOODED_BADLANDS_PLATEAU, Biome.MODIFIED_WOODED_BADLANDS_PLATEAU)),
    S_TUNDRA(List.of(Biome.SNOWY_TUNDRA, Biome.SNOWY_TAIGA, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.SNOWY_BEACH)),
    S_TAIGA(List.of(Biome.TAIGA, Biome.TAIGA_MOUNTAINS, Biome.TAIGA_HILLS)),
    S_SAVANNA(List.of(Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.SHATTERED_SAVANNA, Biome.SHATTERED_SAVANNA_PLATEAU)),
    S_MOUNTAINS(List.of(Biome.MOUNTAINS, Biome.MOUNTAIN_EDGE, Biome.GRAVELLY_MOUNTAINS, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.SNOWY_MOUNTAINS, Biome.WOODED_MOUNTAINS)),
    S_OCEAN(List.of(Biome.OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_WARM_OCEAN, Biome.WARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.DEEP_OCEAN));
    
    /**
     * {@link Biome Biomes}, represented by this simplified biome.
     */
    private final List<Biome> biomes;
    
    SimpleBiomes(List<Biome> biomes) { this.biomes = biomes; }
    
    /**
     * Check if {@link Biome} name represents simplified biome.
     *
     * @param biome {@link Biome} name to check
     * @return {@code true} if biome is simplified biome, otherwise {@code false}
     */
    public static boolean isSimplifiedBiome(String biome) {
        return biome.startsWith("S_");
    }
    
    /**
     * Get a bunch of {@link Biome Biomes}, represented by this simplified biome.
     *
     * @return {@link Biome Biomes} inside current simplified biome
     */
    public List<Biome> getBiomes() {
        return this.biomes.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
    
}