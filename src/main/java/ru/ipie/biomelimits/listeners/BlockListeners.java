package ru.ipie.biomelimits.listeners;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.ipie.biomelimits.BiomeLimits;

import java.util.List;

public class BlockListeners implements Listener {
    @EventHandler
    public void onCrop(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Material material = block.getBlockData().getMaterial();
        Biome biome = block.getBiome();
    
        List<Biome> whitelistedBiomes = BiomeLimits.getWhitelistedBlocks().get(material);
    
        if (whitelistedBiomes != null && !whitelistedBiomes.contains(biome)) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }
}