package xyz.ipiepiepie.biomelimits.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.ConsoleCommandSender;

import xyz.ipiepiepie.biomelimits.BiomeLimits;
import xyz.ipiepiepie.biomelimits.BiomeLimitsAPI;
import xyz.ipiepiepie.biomelimits.LimitsLogger;
import xyz.ipiepiepie.biomelimits.config.MessagesConfig;
import xyz.ipiepiepie.biomelimits.config.SettingsConfig;
import xyz.ipiepiepie.biomelimits.object.LimitedBlock;
import xyz.ipiepiepie.biomelimits.util.Message;

import java.util.List;

import static xyz.ipiepiepie.biomelimits.config.MessagesConfig.*;
import static xyz.ipiepiepie.biomelimits.config.SettingsConfig.LIMITED_BLOCK_INFO_BIOMES_PER_LINE;

/**
 * /biomelimits <i>(/bl)</i> command
 * <p>
 * Command for admins with several useful sub-commands to interact with {@link BiomeLimits} system.
 */
public class BiomeLimitsCommand {
    
    /**
     * /biomelimits
     */
    public static CommandAPICommand getCommand() {
        return new CommandAPICommand("biomelimits")
                .withAliases("bl")
                .withSubcommand(info())
                .withSubcommand(reload());
    }
    
    /// INFO ///
    
    /**
     * /biomelimits info [limited block]
     * <p>
     * Permission: <i>biomelimits.info</i>
     */
    private static CommandAPICommand info() {
        return new CommandAPICommand("info")
                .withPermission("biomelimits.info")
                .withArguments(limitedBlockArgument())
                .executes((sender, args) -> {
                    LimitedBlock block = (LimitedBlock) args.get(0);
                    assert block != null;
                    
                    // send info message
                    MessagesConfig.send(sender, new Message(MSG_INFO)
                            .withPrefix(false)
                            .addPlaceholder("%block%", block.getName())
                            .addPlaceholder("%whitelisted_biomes_count%", String.valueOf(block.getWhitelistedBiomes().size()))
                            .addPlaceholder("%whitelisted_biomes%", buildBiomesString(block.getWhitelistedBiomes()))
                            .addPlaceholder("%blacklisted_biomes_count%", String.valueOf(block.getBlacklistedBiomes().size()))
                            .addPlaceholder("%blacklisted_biomes%", buildBiomesString(block.getBlacklistedBiomes()))
                            .addPlaceholder("%placeable%", block.canBePlaced() ? ChatColor.GREEN + "true" : ChatColor.RED + "false")
                            .addPlaceholder("%destroyable%", block.canBeDestroyed() ? ChatColor.GREEN + "true" : ChatColor.RED + "false")
                    );
                });
    }
    
    /**
     * Build String, containing {@link Biome Biome} names.
     * <p>
     * Puts 4 {@link Biome} names per line.
     *
     * @param biomes list of {@link Biome Biomes}
     *
     * @return {@link Biome} names
     */
    private static String buildBiomesString(List<Biome> biomes) {
        StringBuilder builder = new StringBuilder();
        
        int counter = 0;
        for (String biome : biomes.stream().map(Biome::toString).toList()) {
            builder.append(biome).append(", ");
            
            // go to new line if we reached 'biomes per line' limit (default 4)
            if (++counter % SettingsConfig.get(LIMITED_BLOCK_INFO_BIOMES_PER_LINE) == 0) {
                builder.append("\n");
                
                LimitsLogger.debug("Line limit reached, going to next line...");
            }
        }
        
        // return biomes string without last comma
        return builder.delete(builder.length() - 1, builder.length()).toString();
    }
    
    /// SET ///
    
    // TODO make set sub-commands
    private static CommandAPICommand set() {
        return new CommandAPICommand("set");
    }
    
    /// RELOAD ///
    
    /**
     * /biomelimits reload
     * <p>
     * Permission: <i>biomelimits.reload</i>
     */
    private static CommandAPICommand reload() {
        return new CommandAPICommand("reload")
                .withPermission("biomelimits.reload")
                .executes((sender, args) -> {
                    LimitsLogger.log("Reloading Plugin...");
                    
                    BiomeLimits.getInstance().loadConfigs(); // reload configs
                    BiomeLimitsAPI.getInstance().load(false); // reload API (to refresh limited blocks)
                    
                    // send message (skip sending to console, because console see logs)
                    if (!(sender instanceof ConsoleCommandSender))
                        MessagesConfig.send(sender, new Message(MSG_RELOAD));
                    LimitsLogger.log("Plugin reloaded!");
                });
    }
    
    /*========================================* ARGUMENTS *=========================================*/
    
    // Weapon argument
    private static Argument<LimitedBlock> limitedBlockArgument() {
        return new CustomArgument<>(new StringArgument("block"), info -> {
            LimitedBlock block = BiomeLimitsAPI.getInstance().getLimitedBlock(info.currentInput());
            
            if (block == null)
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown limited block: ").appendArgInput());
            else
                return block;
        }).replaceSuggestions(ArgumentSuggestions.strings(info -> BiomeLimitsAPI.getInstance()
                .getLimitedBlocks()
                .stream()
                .map(LimitedBlock::getName)
                .toArray(String[]::new)
        ));
    }
    
}