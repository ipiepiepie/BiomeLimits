package xyz.nifti.biomelimits.config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.nifti.translate.io.TranslationFiles;

public class BiomeLimitsMessages {
    private static TranslationFiles translatableCfg;

    public static void setTranslatableCfg(TranslationFiles cfg) {
        translatableCfg = cfg;
    }

    public static FileConfiguration getConfigForPlayer(Player player) {
        return translatableCfg.get(player);
    }

    /**
     * Send block place message. <br>
     * Sends unique block place message if exists,
     * otherwise sends default place message.
     *
     * @param player player to send message for
     * @param material material to get unique message if exists
     */
    public static void sendPlaceMessage(Player player, Material material) {
        String messagePath = material != null ? "blocks." + material + ".place_message" : "null";

        player.sendMessage(getConfigForPlayer(player).getString(messagePath) != null
                ? getString(player, messagePath)
                : getString(player, "messages.place_message")
        );
    }

    /**
     * Send block break message. <br>
     * Sends unique block break message if exists,
     * otherwise sends default break message.
     *
     * @param player player to send message for
     * @param material material to get unique message if exists
     */
    public static void sendBreakMessage(Player player, Material material) {
        String messagePath = material != null ? "blocks." + material : ".break_message";

        player.sendMessage(getConfigForPlayer(player).getString(messagePath) != null
                ? getString(player, messagePath)
                : getString(player, "messages.break_message")
        );
    }

    public static void send(CommandSender receiver, String line) {
        if (line == null)
            return;

        receiver.sendMessage(getString(receiver, "messages." + line));
    }

    public static String getString(CommandSender sender, String line) {
        String str = getConfigForPlayer(sender instanceof Player ? (Player) sender : null).getString(line);

        return ChatColor.translateAlternateColorCodes('&', str != null ? str : "null");
    }
}
