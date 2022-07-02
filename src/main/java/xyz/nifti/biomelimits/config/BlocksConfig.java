package xyz.nifti.biomelimits.config;

import com.cryptomorin.xseries.XBiome;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.nifti.biomelimits.BiomeLimits;
import xyz.nifti.biomelimits.util.SimpleBiomes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlocksConfig {
    private static FileConfiguration config;

    public static void load() {
        File configFile = new File(BiomeLimits.getInstance().getDataFolder(), "blocks.yml");

        if (!configFile.exists())
            BiomeLimits.getInstance().saveResource("blocks.yml", false);

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    // ==================================================================================================
    /*
        Getters for values from blocks.yml config
     */
    // ==================================================================================================

    public static Set<String> getLimitedBlocksNames() {
        return config.getKeys(false);
    }

    public static List<Biome> getBlacklistedBiomes(Material block) {
        List<Biome> biomes = new ArrayList<>();

        for (String name : getConfig().getStringList(block.toString() + ".blacklist")) {
            if (SimpleBiomes.isSimplifiedBiome(name)) {
                biomes.addAll(SimpleBiomes.valueOf(name).getBiomes());
                continue;
            }
            try {
                biomes.add(XBiome.valueOf(name).getBiome());
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().severe(String.format("Can't load blacklist biome '%s' for block '%s'", name, block));
            }
        }

        return biomes;
    }

    public static List<Biome> getWhitelistedBiomes(Material block) {
        List<Biome> biomes = new ArrayList<>();

        for (String name : getConfig().getStringList(block.toString() + ".whitelist")) {
            if (SimpleBiomes.isSimplifiedBiome(name)) {
                biomes.addAll(SimpleBiomes.valueOf(name).getBiomes());
                continue;
            }
            try {
                biomes.add(XBiome.valueOf(name).getBiome());
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().severe(String.format("Can't load whitelist biome '%s' for block '%s'", name, block));
            }
        }

        return biomes;
    }

    public static boolean isPlaceDisabled(Material block) {
        return !getConfig().getBoolean(block.toString() + ".place");
    }

    public static boolean isBreakDisabled(Material block) {
        return !getConfig().getBoolean(block.toString() + ".break");
    }
}
