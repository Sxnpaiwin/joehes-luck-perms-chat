package de.joehe.lpc;

import de.joehe.lpc.chat.ChatFormatService;
import de.joehe.lpc.chat.EmojiReplacer;
import de.joehe.lpc.chat.MentionService;
import de.joehe.lpc.chat.UrlLinkifier;
import de.joehe.lpc.commands.JoehesLPCCommand;
import de.joehe.lpc.listener.AsyncChatListener;
import de.joehe.lpc.listener.ConnectionListener;
import de.joehe.lpc.listener.SpigotChatListener;
import de.joehe.lpc.scheduler.Scheduler;
import de.joehe.lpc.scheduler.Schedulers;
import de.joehe.lpc.update.UpdateChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class JoehesLPC extends JavaPlugin {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('§')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private boolean paper;
    private boolean folia;
    private Scheduler scheduler;
    private ChatFormatService chatFormatService;
    private EmojiReplacer emojiReplacer;
    private UrlLinkifier urlLinkifier;
    private MentionService mentionService;

    public static LegacyComponentSerializer getLegacySerializer() {
        return LEGACY_SERIALIZER;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.paper = detectPaper();
        this.folia = detectFolia();
        this.scheduler = Schedulers.create(this);

        // Validate configuration on startup
        validateAndLoadConfig(getServer().getConsoleSender());

        this.chatFormatService = new ChatFormatService(this);
        this.emojiReplacer = new EmojiReplacer(this);
        this.urlLinkifier = new UrlLinkifier(this);
        this.mentionService = new MentionService(this);
        registerCommand();
        registerListeners();
        startUpdateChecker();
    }

    public boolean isPaper() {
        return paper;
    }

    public boolean isFolia() {
        return folia;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void onDisable() {
        if (scheduler != null) {
            scheduler.cancelAll();
        }
    }

    public ChatFormatService getChatFormatService() {
        return chatFormatService;
    }

    public EmojiReplacer getEmojiReplacer() {
        return emojiReplacer;
    }

    public UrlLinkifier getUrlLinkifier() {
        return urlLinkifier;
    }

    public MentionService getMentionService() {
        return mentionService;
    }

    /** Re-reads config-derived state for every service. Call after {@code reloadConfig()}. */
    public void reloadServices() {
        chatFormatService.reload();
        emojiReplacer.reload();
        urlLinkifier.reload();
        mentionService.reload();
    }

    /** @return whether chat formatting is disabled in the given world. */
    public boolean isDisabledWorld(String worldName) {
        for (String world : getConfig().getStringList("disabled-worlds")) {
            if (world.equalsIgnoreCase(worldName)) {
                return true;
            }
        }
        return false;
    }

    /** Resolves a player's display name as a component on either platform. */
    @SuppressWarnings("deprecation") // getDisplayName() is the Spigot fallback
    public Component displayNameOf(Player player) {
        return paper ? player.displayName() : LEGACY_SERIALIZER.deserialize(player.getDisplayName());
    }

    /** Sends a component to a sender, falling back to legacy text on Spigot. */
    public void send(CommandSender target, Component component) {
        if (paper) {
            target.sendMessage(component);
        } else {
            target.sendMessage(LEGACY_SERIALIZER.serialize(component));
        }
    }

    private void registerCommand() {
        PluginCommand command = getCommand("jlpc");
        if (command == null) {
            getLogger().warning("Command 'jlpc' is missing from plugin.yml; commands are unavailable.");
            return;
        }
        JoehesLPCCommand executor = new JoehesLPCCommand(this);
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        if (paper) {
            pluginManager.registerEvents(new AsyncChatListener(this), this);
        } else {
            pluginManager.registerEvents(new SpigotChatListener(this), this);
        }
        pluginManager.registerEvents(new ConnectionListener(this), this);
    }

    private void startUpdateChecker() {
        if (!getConfig().getBoolean("update-checker", true)) {
            return;
        }
        UpdateChecker updateChecker = new UpdateChecker(this);
        getServer().getPluginManager().registerEvents(updateChecker, this);
        updateChecker.checkAsync();
    }

    private boolean detectPaper() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            getLogger().info("Paper API detected — using Adventure chat rendering.");
            return true;
        } catch (ClassNotFoundException notPaper) {
            getLogger().info("Spigot API detected — using legacy chat rendering.");
            return false;
        }
    }

    private boolean detectFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            getLogger().info("Folia detected — using regionized scheduling.");
            return true;
        } catch (ClassNotFoundException notFolia) {
            return false;
        }
    }

    /**
     * Validates the configuration file on disk for YAML syntax errors and MiniMessage parsing errors.
     *
     * @param sender the command sender to report errors to (or ConsoleSender)
     * @return true if the configuration is fully valid; false otherwise
     */
    public boolean validateAndLoadConfig(CommandSender sender) {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            return true; // No file, nothing to validate
        }

        // 1. Check YAML syntax by loading manually first
        YamlConfiguration testConfig = new YamlConfiguration();
        try {
            testConfig.load(configFile);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            send(sender, MiniMessage.miniMessage().deserialize("<red>❌ Invalid YAML syntax in config.yml! Error details: <white>" + errorMsg));
            return false;
        }

        // 2. Perform semantic and MiniMessage syntax validation on the new config
        List<String> errors = new ArrayList<>();

        validateTemplate(testConfig.getString("chat-format"), "chat-format", true, errors);

        ConfigurationSection groupFormats = testConfig.getConfigurationSection("group-formats");
        if (groupFormats != null) {
            for (String key : groupFormats.getKeys(false)) {
                validateTemplate(groupFormats.getString(key), "group-formats." + key, true, errors);
            }
        }

        ConfigurationSection trackFormats = testConfig.getConfigurationSection("track-formats");
        if (trackFormats != null) {
            for (String key : trackFormats.getKeys(false)) {
                validateTemplate(trackFormats.getString(key), "track-formats." + key, true, errors);
            }
        }

        validateTemplate(testConfig.getString("mentions.highlight-format"), "mentions.highlight-format", false, errors);
        validateTemplate(testConfig.getString("mentions.actionbar-text"), "mentions.actionbar-text", false, errors);
        validateTemplate(testConfig.getString("link-urls.style"), "link-urls.style", false, errors);
        validateTemplate(testConfig.getString("reload-message"), "reload-message", false, errors);

        if (testConfig.getBoolean("join-messages.enabled", false)) {
            validateTemplate(testConfig.getString("join-messages.format"), "join-messages.format", false, errors);
            if (testConfig.getBoolean("join-messages.first-join.enabled", false)) {
                validateTemplate(testConfig.getString("join-messages.first-join.format"), "join-messages.first-join.format", false, errors);
            }
        }
        if (testConfig.getBoolean("quit-messages.enabled", false)) {
            validateTemplate(testConfig.getString("quit-messages.format"), "quit-messages.format", false, errors);
        }
        if (testConfig.getBoolean("death-messages.enabled", false)) {
            validateTemplate(testConfig.getString("death-messages.format"), "death-messages.format", false, errors);
        }

        // If there were any errors, print them clearly and report failure
        if (!errors.isEmpty()) {
            send(sender, MiniMessage.miniMessage().deserialize("<red>❌ Configuration validation failed! Please fix the following:"));
            for (String err : errors) {
                send(sender, MiniMessage.miniMessage().deserialize("<dark_gray>- <yellow>" + err));
            }
            return false;
        }

        return true;
    }

    private void validateTemplate(String template, String path, boolean requireMessageToken, List<String> errors) {
        if (template == null || template.isEmpty()) {
            return;
        }
        // Test if MiniMessage can parse the string cleanly
        try {
            MiniMessage.miniMessage().deserialize(template);
        } catch (Exception e) {
            errors.add(path + ": Invalid MiniMessage tags (" + e.getMessage() + ")");
        }

        // Verify if required token {message} is present
        if (requireMessageToken && !template.contains("{message}")) {
            errors.add(path + ": Missing required placeholder `{message}`");
        }
    }
}
