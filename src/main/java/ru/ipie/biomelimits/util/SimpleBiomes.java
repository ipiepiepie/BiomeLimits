package ru.ipie.biomelimits.util;

import com.cryptomorin.xseries.XBiome;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.List;

public enum SimpleBiomes {
    S_FOREST(Arrays.asList(Biome.FOREST, Biome.WOODED_HILLS, Biome.FLOWER_FOREST)),
    S_BIRCH_FOREST(Arrays.asList(Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.TALL_BIRCH_FOREST, Biome.TALL_BIRCH_HILLS)),
    S_DARK_FOREST(Arrays.asList(Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS)),
    S_JUNGLE(Arrays.asList(Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE, XBiome.BAMBOO_JUNGLE.getBiome(), XBiome.BAMBOO_JUNGLE_HILLS.getBiome())),
    S_SWAMP(Arrays.asList(Biome.SWAMP, Biome.SWAMP_HILLS)),
    S_PLAINS(Arrays.asList(Biome.PLAINS, Biome.SUNFLOWER_PLAINS)),
    S_DESERT(Arrays.asList(Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES)),
    S_BADLANDS(Arrays.asList(Biome.BADLANDS, Biome.BADLANDS_PLATEAU, Biome.ERODED_BADLANDS, Biome.MODIFIED_BADLANDS_PLATEAU, Biome.WOODED_BADLANDS_PLATEAU, Biome.MODIFIED_WOODED_BADLANDS_PLATEAU)),
    S_TUNDRA(Arrays.asList(Biome.SNOWY_TUNDRA, Biome.SNOWY_TAIGA, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.SNOWY_BEACH)),
    S_TAIGA(Arrays.asList(Biome.TAIGA, Biome.TAIGA_MOUNTAINS, Biome.TAIGA_HILLS)),
    S_SAVANNA(Arrays.asList(Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.SHATTERED_SAVANNA, Biome.SHATTERED_SAVANNA_PLATEAU)),
    S_MOUNTAINS(Arrays.asList(Biome.MOUNTAINS, Biome.MOUNTAIN_EDGE, Biome.GRAVELLY_MOUNTAINS, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.SNOWY_MOUNTAINS, Biome.WOODED_MOUNTAINS));

    private final List<Biome> biomes;

    SimpleBiomes(List<Biome> biomes) { this.biomes = biomes; }

  
    public static boolean isSimplifiedBiome(String biome) {
        return biome.startsWith("S_");
    }
  
    public List<Biome> getBiomes() {
        return this.biomes;
    }
}