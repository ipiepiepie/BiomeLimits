package ru.ipie.biomelimits;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ipie.biomelimits.listeners.BlockListeners;
import ru.ipie.biomelimits.util.SimpleBiomes;

import java.util.*;
import java.util.stream.Collectors;

public final class BiomeLimits extends JavaPlugin {
    private static BiomeLimits instance;
    private static Map<Material, List<Biome>> whitelistedBlocks;

    public static BiomeLimits getInstance() {
        return instance;
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        instance = this;
        whitelistedBlocks = createWhitelistedBlocks();

        Bukkit.getPluginManager().registerEvents(new BlockListeners(), this);
    }

    public static Map<Material, List<Biome>> getWhitelistedBlocks() {
        return whitelistedBlocks;
    }

    private Map<Material, List<Biome>> createWhitelistedBlocks() {
        Map<Material, List<Biome>> blacklistedBlocks = new HashMap<>();

        for (LinkedHashMap<String, List<String>> input : (List<LinkedHashMap<String, List<String>>>) getInstance().getConfig().getList("whitelisted-blocks")) {
            blacklistedBlocks.putAll(input.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> XMaterial.valueOf(e.getKey()).parseMaterial(), e -> convertToBiomes(e.getValue()))));
        }

        return blacklistedBlocks;
    }

    private List<Biome> convertToBiomes(List<String> names) {
        List<Biome> biomes = new ArrayList<>();

        for (String name : names) {
            if (SimpleBiomes.isSimplifiedBiome(name)) {
                biomes.addAll(SimpleBiomes.valueOf(name).getBiomes());
                continue;
            }
            try {
                biomes.add(XBiome.valueOf(name).getBiome());
            } catch (IllegalArgumentException ignore) {}
        }

        return biomes;
    }
}
