package xyz.nifti.biomelimits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nifti.biomelimits.command.BiomeLimitsCommand;
import xyz.nifti.biomelimits.config.BiomeLimitsMessages;
import xyz.nifti.biomelimits.config.BlocksConfig;
import xyz.nifti.biomelimits.listener.BlockListener;
import xyz.nifti.biomelimits.object.LimitedBlock;
import xyz.nifti.translate.NiftiTranslate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeLimits extends JavaPlugin {
    private static BiomeLimits instance;
    private final Map<Material, LimitedBlock> limitedBlocks = new ConcurrentHashMap<>();

    public static BiomeLimits getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        BlocksConfig.load();
        BiomeLimitsMessages.setTranslatableCfg(NiftiTranslate.getInstance().initialiseTranslationFiles(this));

        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);

        this.loadLimitedBlocks(); // Initialise limited blocks

        getCommand("biomelimits").setExecutor(new BiomeLimitsCommand());
    }

    /**
     * Load all blocks from {@link BlocksConfig} <br>
     * Clears limited blocks list on start, so can be used on config reloading,
     * to update changes without server reloading.
     */
    public void loadLimitedBlocks() {
        this.limitedBlocks.clear(); // Clear map for reloads

        for (String name : BlocksConfig.getLimitedBlocksNames()) {
            try {
                Material material = Material.valueOf(name);
                LimitedBlock block = new LimitedBlock(material, BlocksConfig.isPlaceDisabled(material), BlocksConfig.isBreakDisabled(material));

                // Add blacklisted and whitelisted biomes
                block.getBlacklistedBiomes().addAll(BlocksConfig.getBlacklistedBiomes(material));
                block.getWhitelistedBiomes().addAll(BlocksConfig.getWhitelistedBiomes(material));

                limitedBlocks.put(material, block);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().severe(String.format("Can't load limits for block '%s'", name));
            }
        }

        getLogger().info(String.format("Successfully loaded %d limited blocks!", limitedBlocks.size()));
    }

    /**
     * Get limited block by {@link Material}
     *
     * @param material material to get block with
     * @return limited block if exists, otherwise null
     */
    public LimitedBlock getLimitedBlock(Material material) {
        return limitedBlocks.get(material);
    }
}
