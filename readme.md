<div align="center">

```text
       _                 _                 _      _____   _____ 
      | |               | |               | |    |  __ \ / ____|
      | | ___   ___  ___| |__   ___  ___  | |    | |__) | |     
  _   | |/ _ \ / _ \/ __| '_ \ / _ \/ __| | |    |  ___/| |     
 | |__| | (_) |  __/\__ \ | | |  __/\__ \ | |____| |    | |____ 
  \____/ \___/ \___||___/_| |_|\___||___/ |______|_|     \_____|
```

# JoehesLPC
**A modern, highly-optimized LuckPerms Chat Formatter natively targeting Minecraft 1.21+**

[![Minecraft Support](https://img.shields.io/badge/Minecraft-1.21+-red?style=for-the-badge&logo=minecraft&logoColor=white)](#)
[![Java Version](https://img.shields.io/badge/Java-25%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](#)
[![License](https://img.shields.io/github/license/Sxnpaiwin/joehes-luck-perms-chat?style=for-the-badge&color=blue)](#)
[![Modrinth Page](https://img.shields.io/badge/Modrinth-JoehesLPC-00C853?style=for-the-badge&logo=modrinth&logoColor=white)](https://modrinth.com/user/Sxnpaiwin)

---

</div>

## 🚀 Key Improvements & Patches

*   **🔒 Complete Style Leakage Protection**: Solved a critical Minecraft rendering bug where styles like `&k` (obfuscated/enchanted text), bold `&l`, or italic `&o` from prefixes/suffixes would leak past color changes and corrupt subsequent player names or text.
*   **🔌 PlaceholderAPI Color Resolution**: Resolved issues where legacy colors and custom hex codes expanded from PAPI placeholders (such as LuckPerms group suffixes) would display as raw, unformatted text in chat.
*   **⚡ Forward-Compatible API**: Restructured to compile natively on Paper `1.21` APIs, ensuring fully stable out-of-the-box forward compatibility with all newer server releases.

---

## 🧩 Compatibility

| Component | Target / Requirement |
|---|---|
| **Minecraft Server** | `1.21` and all newer versions (`1.21+`) |
| **Supported Software** | Paper (Recommended), Folia, or Spigot |
| **Java Environment** | `25+` |

---

## 🔧 Requirements

*   [LuckPerms](https://luckperms.net/) *(Required)* – Core permissions and metadata provider.
*   [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) *(Optional)* – To integrate extra placeholder variables inside chat formats.

---

## ✅ Features

*   **💬 Modern Chat Formatting**: Full [MiniMessage](https://docs.advntr.dev/minimessage/format.html) rendering engine with group and track formats.
*   **👥 Social System**: Interactive `@Mention` pings (sound/action-bar on Paper), clickable `openUrl` links, and configurable text-to-glyph emoji shortcuts (e.g., `:heart:` → ❤).
*   **🛡️ Moderation Toolkit**: Anti-spam cooldowns, repeated-message blockers, caps filters, profanity filters, and anti-advertising safeguards.
*   **🔇 Integrated Mute System**: Fully managed local temp-mutes and permanent mutes with `/jlpc mute`.

---

## 📂 Configuration & Commands

<details>
<summary><b>📂 Click to expand default <code>config.yml</code></b></summary>

```yaml
# Main chat format (MiniMessage!)
chat-format: "{prefix}{name}<dark_gray> »<reset> {message}"

# Per-group formats (optional)
group-formats:
#  default: "<gray>[User]</gray> {name}<dark_gray> »<reset> {message}"
#  admin: "<red>[Admin]</red> {name}<dark_gray> »<reset> {message}"

# Per-track formats (optional)
track-formats:
#  staff_track: "<gold>[Staff]</gold> {name}<dark_gray> »<reset> {message}"

# Enable the [item] placeholder (shows held item details on hover)
use-item-placeholder: true

# Allow <gradient> / <rainbow> for players with joeheslpc.chatcolor
allow-gradient-tags: true

# Worlds where JoehesLPC does NOT format chat
disabled-worlds: []

# Check Modrinth for updates on startup
update-checker: true

# Reload message
reload-message: "<green>Reloaded JoehesLPC configuration!"
```

</details>

<details>
<summary><b>⌨️ Click to expand Commands & Permissions</b></summary>

| Command | Permission | Description |
|---|---|---|
| `/jlpc reload` | `joeheslpc.reload` | Reloads the configuration |
| `/jlpc version` | – | Shows the current installed version |
| `/jlpc help` | – | Displays available commands |
| `/jlpc mute <player> [duration]` | `joeheslpc.mute` | Mutes a player (e.g. `10m`, `2h`, or permanent) |
| `/jlpc unmute <player>` | `joeheslpc.mute` | Unmutes a player |

*Note: The command supports aliases `/lpc` and `/joeheslpc` for legacy compatibility.*

</details>

---

## 🛠️ Building & Compilation

Requires **JDK 25** to compile.

```bash
./gradlew shadowJar
# Compiled jar will be located at build/libs/JoehesLPC-1.21.11.jar
```
