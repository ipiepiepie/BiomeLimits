package xyz.ipiepiepie.biomelimits.listener;

import org.apache.commons.lang.WordUtils;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import xyz.ipiepiepie.biomelimits.BiomeLimitsAPI;
import xyz.ipiepiepie.biomelimits.LimitsLogger;
import xyz.ipiepiepie.biomelimits.config.MessagesConfig;
import xyz.ipiepiepie.biomelimits.object.LimitedBlock;

/**
 * Listens {@link LimitedBlock Limited Block} specified events.
 */
public class BlockListener implements Listener {
    
    /**
     * Limit block destroying in blacklisted/non-whitelisted {@link Biome Biomes} if it's {@link LimitedBlock#canBeDestroyed() limited}.
     * <p>
     * Bypass permission: <i>biomelimits.bypass.destroy</i>
     *
     * @see LimitedBlock
     */
    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        // bypass check
        if (event.getPlayer().hasPermission("biomelimits.bypass.destroy"))
            return;

        LimitedBlock limitedBlock = BiomeLimitsAPI.getInstance().getLimitedBlock(event.getBlock().getType());
        Biome biome = event.getBlock().getBiome();

        // skip biome check if current block is destroyable without any limitations
        if (limitedBlock == null || limitedBlock.canBeDestroyed())
            return;
        
        // cancel block destroy if current biome is limited
        if (limitedBlock.isLimitedBiome(biome)) {
            event.setCancelled(true);
            
            // send debug message
            LimitsLogger.debug("Cancelled &7%s&r break event for player &7%s&r in biome &7%s",
                    limitedBlock.getName(),
                    event.getPlayer().getName(),
                    biome.toString()
            );
            
            // send destroy message
            MessagesConfig.send(event.getPlayer(), limitedBlock.getDestroyMessage()
                    .addPlaceholder("%block%", WordUtils.capitalizeFully(limitedBlock.getName()))
                    .addPlaceholder("%biome%", WordUtils.capitalizeFully(biome.toString().replaceAll("_", " ")))
            );
        }
    }
    
    /**
     * Limit block placing in blacklisted/non-whitelisted {@link Biome Biomes} if it's {@link LimitedBlock#canBePlaced() limited}.
     * <p>
     * Bypass permission: <i>biomelimits.bypass.place</i>
     *
     * @see LimitedBlock
     */
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        // bypass check
        if (event.getPlayer().hasPermission("biomelimits.bypass.place"))
            return;
        
        LimitedBlock limitedBlock = BiomeLimitsAPI.getInstance().getLimitedBlock(event.getBlock().getType());
        Biome biome = event.getBlock().getBiome();
        
        // skip biome check if current block is placeable without any limitations
        if (limitedBlock == null || limitedBlock.canBePlaced())
            return;
        
        // cancel block placement if current biome is limited
        if (limitedBlock.isLimitedBiome(biome)) {
            event.setCancelled(true);
            
            // send debug message
            LimitsLogger.debug("Cancelled &7%s&r place event for player &7%s&r in biome &7%s",
                    limitedBlock.getName(),
                    event.getPlayer().getName(),
                    biome.toString()
            );
            
            // send place message
            MessagesConfig.send(event.getPlayer(), limitedBlock.getPlaceMessage()
                    .addPlaceholder("%block%", WordUtils.capitalizeFully(limitedBlock.getName()))
                    .addPlaceholder("%biome%", WordUtils.capitalizeFully(biome.toString().replaceAll("_", " ")))
            );
        }
    }
    
}
