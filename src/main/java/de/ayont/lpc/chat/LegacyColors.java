package de.ayont.lpc.chat;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Single source of truth for the legacy color/format code ({@code &a}, {@code §a}) to
 * MiniMessage tag mapping. Previously this table was copy-pasted across three classes.
 */
public final class LegacyColors {

    /** Immutable, ordered legacy code -> MiniMessage tag mapping (lowercase codes). */
    public static final Map<String, String> LEGACY_TO_MINIMESSAGE;

    /** Matches a legacy code character (any case) following an ampersand. */
    private static final Pattern LEGACY_CODE = Pattern.compile("&([0-9A-Fa-fK-ORk-or])");

    private static final Pattern HEX_COLOR_HASH = Pattern.compile("&#([0-9A-Fa-f]{6})");
    private static final Pattern HEX_COLOR_SPIGOT = Pattern.compile("&[xX]&([0-9A-Fa-f])&([0-9A-Fa-f])&([0-9A-Fa-f])&([0-9A-Fa-f])&([0-9A-Fa-f])&([0-9A-Fa-f])");

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("&0", "<reset><black>");
        map.put("&1", "<reset><dark_blue>");
        map.put("&2", "<reset><dark_green>");
        map.put("&3", "<reset><dark_aqua>");
        map.put("&4", "<reset><dark_red>");
        map.put("&5", "<reset><dark_purple>");
        map.put("&6", "<reset><gold>");
        map.put("&7", "<reset><gray>");
        map.put("&8", "<reset><dark_gray>");
        map.put("&9", "<reset><blue>");
        map.put("&a", "<reset><green>");
        map.put("&b", "<reset><aqua>");
        map.put("&c", "<reset><red>");
        map.put("&d", "<reset><light_purple>");
        map.put("&e", "<reset><yellow>");
        map.put("&f", "<reset><white>");
        map.put("&l", "<bold>");
        map.put("&o", "<italic>");
        map.put("&n", "<underlined>");
        map.put("&m", "<strikethrough>");
        map.put("&k", "<obfuscated>");
        map.put("&r", "<reset>");
        LEGACY_TO_MINIMESSAGE = Map.copyOf(map);
    }

    private LegacyColors() {
    }

    /**
     * Converts legacy color/format codes (using either {@code &} or {@code §}, any case) within the
     * given text to their MiniMessage tag equivalents. Returns the input unchanged when {@code null}
     * or empty.
     *
     * @param input raw text potentially containing legacy codes
     * @return text with legacy codes rewritten as MiniMessage tags
     */
    public static String toMiniMessage(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String out = input.replace('§', '&');

        // Convert hex formats to MiniMessage tags
        out = HEX_COLOR_HASH.matcher(out).replaceAll(match -> "<reset><#" + match.group(1).toLowerCase(Locale.ROOT) + ">");
        out = HEX_COLOR_SPIGOT.matcher(out).replaceAll(match -> "<reset><#" + (match.group(1) + match.group(2) + match.group(3) + match.group(4) + match.group(5) + match.group(6)).toLowerCase(Locale.ROOT) + ">");

        // Normalise the case of code characters (e.g. &A -> &a) so uppercase codes are also converted.
        out = LEGACY_CODE.matcher(out).replaceAll(match -> "&" + match.group(1).toLowerCase(Locale.ROOT));
        for (Map.Entry<String, String> entry : LEGACY_TO_MINIMESSAGE.entrySet()) {
            out = out.replace(entry.getKey(), entry.getValue());
        }
        return out;
    }
}
