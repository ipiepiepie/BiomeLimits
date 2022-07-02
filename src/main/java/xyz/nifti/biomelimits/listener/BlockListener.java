package xyz.nifti.biomelimits.listener;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Crops;
import xyz.nifti.biomelimits.BiomeLimits;
import xyz.nifti.biomelimits.config.BiomeLimitsMessages;
import xyz.nifti.biomelimits.object.LimitedBlock;

public class BlockListener implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission("biomelimits.bypass"))
            return; // bypass check

        LimitedBlock limitedBlock = BiomeLimits.getInstance().getLimitedBlock(event.getBlock().getBlockData().getMaterial());
        Biome biome = event.getBlock().getBiome();

        if (limitedBlock == null || !limitedBlock.isBreakDisabled())
            return;

        if (!limitedBlock.getWhitelistedBiomes().contains(biome) || limitedBlock.getBlacklistedBiomes().contains(biome)) {
            event.setDropItems(false);
            event.setCancelled(true);
            BiomeLimitsMessages.sendBreakMessage(event.getPlayer(), limitedBlock.getMaterial());
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("biomelimits.bypass"))
            return;

        LimitedBlock limitedBlock = BiomeLimits.getInstance().getLimitedBlock(event.getBlockPlaced().getBlockData().getMaterial());
        Biome biome = event.getBlockPlaced().getBiome();

        if (limitedBlock == null || !limitedBlock.isPlaceDisabled())
            return;

        if (!limitedBlock.getWhitelistedBiomes().contains(biome) || limitedBlock.getBlacklistedBiomes().contains(biome)) {
            event.setBuild(false);
            event.setCancelled(true);
            BiomeLimitsMessages.sendPlaceMessage(event.getPlayer(), limitedBlock.getMaterial());
        }
    }
}
