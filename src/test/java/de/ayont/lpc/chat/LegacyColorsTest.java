package de.ayont.lpc.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LegacyColorsTest {

    @Test
    @DisplayName("converts ampersand color code to MiniMessage tag")
    void toMiniMessage_ampersandColor_converts() {
        assertEquals("<reset><green>Hello", LegacyColors.toMiniMessage("&aHello"));
    }

    @Test
    @DisplayName("normalizes section sign to ampersand before converting")
    void toMiniMessage_sectionSign_converts() {
        assertEquals("<reset><red>Red", LegacyColors.toMiniMessage("§cRed"));
    }

    @Test
    @DisplayName("converts chained format codes")
    void toMiniMessage_chainedCodes_converts() {
        assertEquals("<bold><underlined>Bold", LegacyColors.toMiniMessage("&l&nBold"));
    }

    @Test
    @DisplayName("returns null unchanged")
    void toMiniMessage_null_returnsNull() {
        assertNull(LegacyColors.toMiniMessage(null));
    }

    @Test
    @DisplayName("returns empty string unchanged")
    void toMiniMessage_empty_returnsEmpty() {
        assertEquals("", LegacyColors.toMiniMessage(""));
    }

    @Test
    @DisplayName("leaves plain text untouched")
    void toMiniMessage_plainText_unchanged() {
        assertEquals("no codes here", LegacyColors.toMiniMessage("no codes here"));
    }

    @Test
    @DisplayName("converts uppercase legacy codes")
    void toMiniMessage_uppercaseCodes_converts() {
        assertEquals("<reset><green><bold>Hi", LegacyColors.toMiniMessage("&A&LHi"));
    }

    @Test
    @DisplayName("converts hex color in ampersand-hash format")
    void toMiniMessage_hexHashColor_converts() {
        assertEquals("<reset><#383997><bold>A", LegacyColors.toMiniMessage("&#383997&lA"));
    }

    @Test
    @DisplayName("converts hex color in section-hash format")
    void toMiniMessage_sectionHashColor_converts() {
        assertEquals("<reset><#383997>B", LegacyColors.toMiniMessage("§#383997B"));
    }

    @Test
    @DisplayName("converts hex color in ampersand-spigot format")
    void toMiniMessage_hexSpigotColor_converts() {
        assertEquals("<reset><#383997>C", LegacyColors.toMiniMessage("&x&3&8&3&9&9&7C"));
    }

    @Test
    @DisplayName("converts hex color in section-spigot format")
    void toMiniMessage_sectionSpigotColor_converts() {
        assertEquals("<reset><#383997>D", LegacyColors.toMiniMessage("§x§3§8§3§9§9§7D"));
    }

    @Test
    @DisplayName("converts complex user string with multiple hex colors and legacy codes")
    void toMiniMessage_complexUserString_converts() {
        String input = "&7[&x&0&F&B&0&0&5&d&0&5&licindercrest&x&0&F&6&0&5&4&6D&0&5&3&l' &x&F&0&D&6&5&5";
        String result = LegacyColors.toMiniMessage(input);
        System.out.println("Result: " + result);
    }
}
