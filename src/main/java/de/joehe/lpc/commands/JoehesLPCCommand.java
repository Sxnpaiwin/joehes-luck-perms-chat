package de.joehe.lpc.commands;

import de.joehe.lpc.JoehesLPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoehesLPCCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("reload", "version", "help");
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final JoehesLPC plugin;

    public JoehesLPCCommand(JoehesLPC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(sender);
            case "version" -> handleVersion(sender);
            default -> sendHelp(sender);
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("JoehesLPC.reload")) {
            plugin.send(sender, mini("<red>You don't have permission to do that."));
            return;
        }
        plugin.reloadConfig();
        plugin.reloadServices();
        String raw = plugin.getConfig().getString("reload-message", "<green>Reloaded JoehesLPC configuration!");
        plugin.send(sender, mini(raw));
    }

    @SuppressWarnings("deprecation") // getDescription() is cross-platform
    private void handleVersion(CommandSender sender) {
        plugin.send(sender, mini("<gradient:#B754F4:#FC00FF>JoehesLPC</gradient> <gray>v<white>"
                + plugin.getDescription().getVersion() + "</white> <dark_gray>— <gray>MiniMessage chat formatter."));
    }

    private void sendHelp(CommandSender sender) {
        plugin.send(sender, mini("<gradient:#B754F4:#FC00FF>JoehesLPC</gradient> <gray>commands:"));
        plugin.send(sender, mini("<dark_gray>- <white>/JoehesLPC reload</white> <dark_gray>» <gray>Reload the configuration"));
        plugin.send(sender, mini("<dark_gray>- <white>/JoehesLPC version</white> <dark_gray>» <gray>Show the plugin version"));
    }

    private static Component mini(String raw) {
        return MM.deserialize(raw);
    }

    private static Component mini(String raw, String key, String value) {
        return MM.deserialize(raw, Placeholder.unparsed(key, value));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                       @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return SUBCOMMANDS.stream().filter(sub -> sub.startsWith(prefix)).toList();
        }
        return List.of();
    }
}
