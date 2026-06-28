# joehes-luck-perms-chat ✨

A modern, highly-optimized fork of **LPC (LuckPerms Chat Formatter)** updated to natively target Minecraft **1.21.11** with major bug fixes and formatting improvements.

---

## 🚀 Key Improvements & Fixes

- **1.21.11 Support**: Fully updated dependencies and API configurations to target `1.21.11-R0.1-SNAPSHOT` (Paper).
- **PlaceholderAPI Color Support**: Resolved the issue where legacy color and hex codes expanded from PAPI placeholders (such as custom suffixes) in LuckPerms would print as raw unformatted text.
- **Style Leakage Fixes**: Fixed a critical bug where formatting codes like `&k` (obfuscated/enchanted text), bold, italic, or underline would leak past color changes and corrupt player names or other text. Pre-pending resets to all color codes ensures correct Minecraft legacy color formatting behavior in MiniMessage.

---

## 🧩 Compatibility

| Component | Target / Requirement |
|---|---|
| **Minecraft** | `1.21.11` |
| **Server** | Paper, Folia, or Spigot |
| **Java** | `25+` |

---

## 🔧 Requirements

- [LuckPerms](https://luckperms.net/) *(Required)* – Permissions plugin
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) *(Optional)* – Additional placeholders

---

## ✅ Features

- **MiniMessage Support**: Complete [MiniMessage](https://docs.advntr.dev/minimessage/format.html) support with group- and track-specific formats.
- **Mentions**: Highlight player names in chat with sound/action-bar alerts.
- **Emoji Replacer**: Built-in, configurable shortcuts (e.g. `:heart:` → ❤).
- **Clickable Links**: Automatically converts URLs into clickable `openUrl` links.
- **[item] Placeholder**: Display held item details with hover tooltip on Paper.
- **Moderation Toolkit**: Built-in anti-spam cooldown, repeated-message blocker, caps filter, profanity filter, and anti-advertising.
- **Integrated Mute System**: Commands like `/lpc mute <player>` with duration support.

---

## ⌨️ Commands & Permissions

| Command | Permission | Description |
|---|---|---|
| `/lpc reload` | `lpc.reload` | Reloads the configuration |
| `/lpc version` | – | Shows the current installed version |
| `/lpc help` | – | Displays available commands |
| `/lpc mute <player> [duration]` | `lpc.mute` | Mutes a player (e.g. `10m`, `2h`, or permanent) |
| `/lpc unmute <player>` | `lpc.mute` | Unmutes a player |

---

## ⚙️ Configuration (`config.yml`)

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

# Enable the [item] placeholder
use-item-placeholder: true

# Allow <gradient> / <rainbow> for players with lpc.chatcolor
allow-gradient-tags: true

# Worlds where LPC does NOT format chat
disabled-worlds: []

# Check Modrinth for updates on startup
update-checker: true

# Reload message
reload-message: "<green>Reloaded LPC configuration!"
```

---

## 🛠️ Building

Requirements: JDK 25.

```bash
./gradlew shadowJar
# output: build/libs/LPC-1.21.11.jar
```
