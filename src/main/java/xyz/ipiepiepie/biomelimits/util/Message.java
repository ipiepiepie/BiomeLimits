package xyz.ipiepiepie.biomelimits.util;

import io.github.townyadvanced.commentedconfiguration.setting.TypedValueNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to construct messages. <br>
 * Makes message creation easier
 * <p>
 * {@link #path} - path to message in config <br>
 * {@link #placeholders} - message placeholders <br>
 * {@link #multiline} - determines if message is multiline
 * <p>
 * <b>If message is multiline, then it should be represented as string list in language config!</b>
 *
 * @author ipiepiepie
 */
public class Message {
    private final String path; // path to message
    // placeholders //
    private final Map<String, String> placeholders = new HashMap<>();
    // flags //
    private final boolean multiline;
    private boolean addPrefix = true;
    
    /**
     * Constructor for message.
     * <p>
     * Automatically detects multiline message or not.
     *
     * @param node node to get path to
     */
    public <T> Message(TypedValueNode<T> node) {
        this(node.getPath(), node.getDefaultValue() instanceof List);
    }
    
    /**
     * Constructor for message.
     *
     * @param path path to message in config file
     */
    public Message(String path) {
        this(path, false);
    }
    
    /**
     * Constructor for message.
     *
     * @param path path to message in config file
     * @param multiline {@code true} if message is multiline, otherwise {@code false}
     */
    public Message(String path, boolean multiline) {
        this.path = path;
        this.multiline = multiline;
    }
    
    /*=========================================* MESSAGE METHODS *=========================================*/
    
    /**
     * Get path to message in config.
     *
     * @return path to current message
     */
    public String getPath() {
        return path;
    }
    
    // String Placeholders //
    
    /**
     * Add placeholder to message.
     *
     * @param key placeholder key
     * @param replacement placeholder itself
     *
     * @return message builder.
     */
    public Message addPlaceholder(String key, String replacement) {
        this.placeholders.put(key, replacement);
        return this;
    }
    
    /**
     * Get all message placeholders.
     *
     * @return placeholders.
     */
    public Map<String, String> getPlaceholders() {
        return placeholders;
    }
    
    /// Multiline ///
    
    /**
     * Check if message multiline or not.
     *
     * @return {@code true} if multiline, otherwise {@code false}
     */
    public boolean isMultiline() {
        return multiline;
    }
    
    /// Prefix ///
    
    /**
     * Add prefix or not to this message
     *
     * @param prefix {@code true} or {@code false}
     * @return {@link Message} builder
     */
    public Message withPrefix(boolean prefix) {
        this.addPrefix = prefix;
        return this;
    }
    
    /**
     * Check if current message has prefix.
     *
     * @return {@code true} if message has prefix, otherwise {@code false}
     */
    public boolean hasPrefix() {
        return addPrefix;
    }
    
}
