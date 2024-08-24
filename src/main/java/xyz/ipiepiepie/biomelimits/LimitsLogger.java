package xyz.ipiepiepie.biomelimits;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import xyz.ipiepiepie.biomelimits.config.SettingsConfig;

/**
 * {@link BiomeLimits} logger, used to simplify logging process.
 */
public class LimitsLogger {
    
    /// LOG ///
    
    /**
     * Send log message with placeholders.
     *
     * @param message log message
     * @param args placeholders
     *
     * @see #log(String)
     */
    public static void log(String message, Object... args) {
        log(String.format(message, args));
    }
    
    /**
     * Send log message.
     *
     * @param message log message
     */
    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    /// WARN ///
    
    /**
     * Send warn message with placeholders.
     *
     * @param message warn message
     * @param args placeholders
     *
     * @see #warn(String)
     */
    public static void warn(String message, Object... args) {
        warn(String.format(message, args));
    }
    
    /**
     * Send warn message.
     *
     * @param message warn message
     */
    public static void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', message));
    }
    
    /// ERROR ///
    
    /**
     * Send error message with placeholders.
     *
     * @param message error message
     * @param args placeholders
     *
     * @see #err(String)
     */
    public static void err(String message, Object... args) {
        err(String.format(message, args));
    }
    
    /**
     * Send error message.
     *
     * @param message error message
     */
    public static void err(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + ChatColor.translateAlternateColorCodes('&', message));
    }
    
    /// DEBUG ///
    
    /**
     * Send debug message with placeholders.
     *
     * @param message debug message
     * @param args placeholders
     *
     * @see #debug(String)
     */
    public static void debug(String message, Object... args) {
        debug(String.format(message, args));
    }
    
    /**
     * Send debug message.
     * <p>
     * Debug works only if {@link SettingsConfig#DEBUG debug mode} is enabled.
     *
     * @param message debug message
     */
    public static void debug(String message) {
        if (SettingsConfig.get(SettingsConfig.DEBUG))
            log("&6[DEBUG]&r " + message);
    }
    
}
