package de.joehe.lpc.listener;

import de.joehe.lpc.JoehesLPC;
import de.joehe.lpc.chat.ChatFormatService;
import de.joehe.lpc.chat.ItemPlaceholder;
import de.joehe.lpc.chat.MentionService;
import de.joehe.lpc.moderation.ModResult;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Legacy Spigot chat listener. Moderates and decorates the message, renders through the shared
 * {@link ChatFormatService}, and bakes the result into the chat format as a legacy string.
 * Hover/click cannot survive legacy serialization, so URL links are styled-only on Spigot.
 */
public class SpigotChatListener implements Listener {

    private final JoehesLPC plugin;
    private final ChatFormatService service;

    public SpigotChatListener(JoehesLPC plugin) {
        this.plugin = plugin;
        this.service = plugin.getChatFormatService();
    }

    @SuppressWarnings("deprecation") // AsyncPlayerChatEvent is the only chat hook on Spigot
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.isDisabledWorld(player.getWorld().getName())) {
            return;
        }

        ModResult moderation = plugin.getModerationService().process(player, event.getMessage());
        if (moderation.isBlocked()) {
            event.setCancelled(true);
            if (moderation.notice() != null) {
                plugin.send(player, moderation.notice());
            }
            return;
        }
        String effectiveRaw = moderation.action() == ModResult.Action.TRANSFORM ? moderation.text() : event.getMessage();

        boolean allowColor = player.hasPermission("JoehesLPC.chatcolor");
        Component base = service.messageComponent(effectiveRaw, allowColor);
        base = plugin.getEmojiReplacer().apply(player, base);
        base = plugin.getUrlLinkifier().apply(player, base, false);

        MentionService.Result mention = plugin.getMentionService()
                .highlight(base, MentionService.onlineNames(plugin.getServer().getOnlinePlayers()));
        plugin.getMentionService().pingAll(mention.mentioned(), player.getName());

        Component rendered = service.render(player, mention.message(), plugin.displayNameOf(player));
        rendered = ItemPlaceholder.apply(plugin, player, rendered, false);

        // Bake the rendered line into the format; escape % so the server's String.format is safe.
        String legacy = JoehesLPC.getLegacySerializer().serialize(rendered).replace("%", "%%");
        event.setFormat(legacy);
    }
}
