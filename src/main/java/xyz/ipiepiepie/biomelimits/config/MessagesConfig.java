package xyz.ipiepiepie.biomelimits.config;

import de.themoep.minedown.MineDown;

import io.github.townyadvanced.commentedconfiguration.CommentedConfiguration;
import io.github.townyadvanced.commentedconfiguration.setting.CommentedNode;
import io.github.townyadvanced.commentedconfiguration.setting.Settings;
import io.github.townyadvanced.commentedconfiguration.setting.SimpleNode;
import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import xyz.ipiepiepie.biomelimits.BiomeLimits;
import xyz.ipiepiepie.biomelimits.LimitsLogger;
import xyz.ipiepiepie.biomelimits.util.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessagesConfig {
    private static final List<CommentedNode> nodes = new ArrayList<>();
    private static Settings settings;
    
    /**
     * Load current config.
     * <p>
     * It'll refresh all config nodes and commentaries.
     */
    public static void load() {
        File configFile = new File(BiomeLimits.getInstance().getDataFolder(), "messages.yml");
        boolean init = !configFile.exists(); // check if it's config creation (used to load limited block template on creation)
        
        settings = new Settings(configFile.toPath(), BiomeLimits.getInstance(), init ? initTemplateNodes() : nodes);
        
        // try to load config
        if (!settings.load()) {
            LimitsLogger.err("&8(Config)&c Can't load &7messages.yml&c config!");
            LimitsLogger.err("Please, check your filesystem permissions settings");
            
            // disable plugin
            Bukkit.getPluginManager().disablePlugin(BiomeLimits.getInstance());
            return;
        }
        
        // save config to file (otherwise we'll have empty file)
        settings.save();
        LimitsLogger.log("&8(Config)&r Loaded &7messages.yml&r config!");
    }
    
    public static CommentedConfiguration getConfig() {
        return settings.getConfig();
    }
    
    /*================================================================================================*/
    
    /// SINGLE-LINE MESSAGE ///
    
    /**
     * Get message node from {@link #getConfig() config}. <br>
     * Searches message in {@code messages} configuration section. <br>
     * Adds prefix to message <i>(optional)</i>.
     * <p>
     * Supports {@link MineDown}.
     *
     * @param message message
     * @return translated message by selected path with replaced placeholders
     */
    public static BaseComponent[] getMessage(Message message) {
        if (message == null) return TextComponent.fromLegacyText("null");
        String prefix = message.hasPrefix() ? get(MSG_PREFIX) : ""; // add prefix if needed
        
        // parse message with placeholders and MineDown support
        return MineDown.parse(prefix + setPlaceholders(getString(message.getPath()), message.getPlaceholders()));
    }
    
    /**
     * Send {@link Message} to {@link CommandSender}. <br>
     * Use null message, to cancel message sending (used in some command parsers)
     *
     * @param receiver player to send message for
     * @param message  message to send
     * @see #getMessage(Message)
     */
    public static void send(CommandSender receiver, Message message) {
        if (message == null) return;
        
        if (message.isMultiline())  // send multiline
            receiver.spigot().sendMessage(getMultilineMessage(message, message.hasPrefix()));
        else  // send single-line
            receiver.spigot().sendMessage(getMessage(message));
    }
    
    /// MULTILINE MESSAGE ///
    
    /**
     * Get message node from {@link #getConfig() config}. <br>
     * Searches message in {@code messages} configuration section. <br>
     * Adds prefix to message.
     * <p>
     * Supports {@link MineDown}.
     *
     * @param message message
     * @param addPrefix determines if this Message should contain prefix
     * @return translated message by selected path with replaced placeholders
     */
    public static BaseComponent[] getMultilineMessage(Message message, boolean addPrefix) {
        if (message == null) return TextComponent.fromLegacyText("null");
        String prefix = addPrefix ? get(MSG_PREFIX) : "";
        StringBuilder builder = new StringBuilder();
        
        for (String line : getStringList(message.getPath(), message.getPlaceholders()))
            builder.append(line).append("\n");
        
        // skip if Builder has zero length (message not found)
        if (builder.isEmpty()) return TextComponent.fromLegacyText("null");
        
        return MineDown.parse(prefix + builder.substring(0, builder.length() - 1));
    }
    
    /// BASE METHODS ///
    
    /**
     * Get String {@link List} from config with parsed colors and placeholders.
     *
     * @param path path to String List
     * @param placeholders placeholders to parse
     *
     * @return parsed String List with colors and placeholders
     */
    public static List<String> getStringList(String path, Map<String, String> placeholders) {
        List<String> list = getConfig().getStringList(path); // get String List
        
        // parse placeholders and colors
        return list.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .map(line -> setPlaceholders(line, placeholders))
                .collect(Collectors.toList());
    }
    
    /**
     * Set placeholders for {@link Message}.
     *
     * @param string message itself
     * @param placeholders message placeholders
     *
     * @return formatted message with placeholders.
     */
    private static String setPlaceholders(String string, Map<String, String> placeholders) {
        for (Map.Entry<String, String> placeholder : placeholders.entrySet())
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        return string;
    }
    
    /**
     * Get String from {@link #getConfig() config} <br>
     * Doesn't use {@code sender} field in this implementation.
     *
     * @param path path for this string in config
     * @return colorized via {@link ChatColor} String
     */
    public static String getString(String path) {
        String str = getConfig().getString(path);
        
        return ChatColor.translateAlternateColorCodes('&', str != null ? str : "null");
    }
    
    /*================================================================================================*/
    
    /**
     * Getter for config nodes.
     */
    public static <T> T get(TypedValueNode<T> node) {
        return settings.get(node);
    }
    
    /**
     * Builder to construct config nodes.
     */
    private static <T> TypedValueNode<T> node(TypedValueNode<T> node) {
        nodes.add(node);
        return node;
    }
    
    /*==========================================* CONFIG NODES *======================================*/
    
    private static List<CommentedNode> initTemplateNodes() {
        node(SimpleNode
                .builder("blocks.carrots", Object.class)
                .comment("# unique destroy and place messages for carrot. This template can be removed/edited.")
                .build()
        );
        
        node(SimpleNode
                .builder("blocks.carrots.msg_limited_destroy", String.class)
                .comment("# destroy cancelled message for carrot")
                .comment("# [Placeholders]")
                .comment("# · %biome% - current player's biome")
                .defaultValue("&cYou can't harvest &4Carrot&c in &4%biome%&c biome!")
                .build()
        );
        
        node(SimpleNode
                .builder("blocks.carrots.msg_limited_place", String.class)
                .comment("# place cancelled message for carrot")
                .comment("# [Placeholders]")
                .comment("# · %biome% - current player's biome")
                .defaultValue("&cYou can't plant &4Carrot&c in &4%biome%&c biome!")
                .build()
        );
        
        return nodes;
    }
    
    /// MESSAGES ///
    
    private static final TypedValueNode<Object> BLOCKS = node(SimpleNode
            .builder("blocks", Object.class)
            .comment("")
            .comment("")
            .comment("# In this section you can define unique destroy/place cancel messages")
            .comment("# for each limited block.")
            .comment("# If you don't want to have unique messages, you can delete whole 'blocks' section.")
            .comment("# [!] Block name in this section must be the same, as in blocks.yml (case sensitive)")
            .build());
    
    private static final TypedValueNode<Object> MESSAGES = node(SimpleNode
            .builder("messages", Object.class)
            .comment("#############################################################")
            .comment("# +-------------------------------------------------------+ #")
            .comment("# |                    Messages config                    | #")
            .comment("# +-------------------------------------------------------+ #")
            .comment("#                                                           #")
            .comment("# Here you can find all BiomeLimits messages.               #")
            .comment("#                                                           #")
            .comment("# You can also define unique destroy/place messages for     #")
            .comment("# some limited blocks in 'blocks' section.                  #")
            .comment("#                                                           #")
            .comment("# [!] Some of the messages support built-in placeholders.   #")
            .comment("# If exists, these comments are noted in comments.          #")
            .comment("#                                                           #")
            .comment("# This plugin supports MineDown                             #")
            .comment("# Read more: https://github.com/Phoenix616/MineDown#syntax  #")
            .comment("#                                                           #")
            .comment("#############################################################")
            .comment("")
            .comment("")
            .build());
    
    public static final TypedValueNode<String> MSG_PREFIX = node(SimpleNode
            .builder("messages.prefix", String.class)
            .defaultValue("&8[&9BiomeLimits&8] &r")
            .build());
    
    // INFO //
    
    public static final TypedValueNode<List> MSG_INFO = node(SimpleNode
            .builder("messages.msg_info", List.class)
            .comment("")
            .comment("# Limited Block info message")
            .comment("# [Placeholders]")
            .comment("# · %block% - block Material name")
            .comment("# · %whitelisted_biomes_count% - count of whitelisted biomes")
            .comment("# · %whitelisted_biomes% - list of whitelisted biomes")
            .comment("# · %blacklisted_biomes_count% - count of blacklisted biomes")
            .comment("# · %blacklisted_biomes% - list of blacklisted biomes")
            .comment("# · %destroyable% - can block be destroyed in limited biomes")
            .comment("# · %placeable% - can block be placed in limited biomes")
            .defaultValue(List.of(
                    "&8==========  &6%block%  &8==========",
                    "&7Whitelisted Biomes: &6[%whitelisted_biomes_count% Biomes](color=gold hover=&2%whitelisted_biomes%)",
                    "&7Blacklisted Biomes: &6[%blacklisted_biomes_count% Biomes](color=gold hover=&4%blacklisted_biomes%)",
                    "",
                    "[&7Destroyable: &6%destroyable%](hover=&7Can block be destroyed in limited biomes)",
                    "[&7Placeable: &6%placeable%](hover=&7Can block be placed in limited biomes)"
            ))
            .build());
    
    /// CANCEL MESSAGES ///
    
    public static final TypedValueNode<String> MSG_LIMITED_DESTROY = node(SimpleNode
            .builder("messages.msg_limited_destroy", String.class)
            .comment("")
            .comment("")
            .comment("# Destroy error for limited block.")
            .comment("# [!] Can be overridden by unique destroy message in 'blocks' section below.")
            .comment("# [Placeholders]")
            .comment("# · %block% - limited block")
            .comment("# · %biome% - current player's biome")
            .defaultValue("&cYou can't destroy &4%block%&c in &4%biome%&c biome!")
            .build());
    
    public static final TypedValueNode<String> MSG_LIMITED_PLACE = node(SimpleNode
            .builder("messages.msg_limited_place", String.class)
            .comment("")
            .comment("# Place error for limited block.")
            .comment("# [!] Can be overridden by unique place message in 'blocks' section below.")
            .comment("# [Placeholders]")
            .comment("# · %block% - limited block")
            .comment("# · %biome% - current player's biome")
            .defaultValue("&cYou can't place &4%block%&c in &4%biome%&c biome!")
            .build());
    
    /// RELOAD ///
    
    public static final TypedValueNode<String> MSG_RELOAD = node(SimpleNode
            .builder("messages.msg_reload", String.class)
            .comment("")
            .comment("")
            .comment("# Plugin reload message.")
            .defaultValue("&aPlugin reloaded!")
            .build());
    
    /*================================================================================================*/
}
