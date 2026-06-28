```
  _     ____   ____ 
 | |   |  _ \ / ___|
 | |   | |_) | |    
 | |___|  __/| |___ 
 |_____|_|    \____|
                    
```

# joehes-luck-perms-chat ＼(≧▽≦)／

A modern, highly-optimized **LuckPerms Chat Formatter** natively targeting Minecraft **1.21+** (including `1.21.11`) with critical formatting and style bugfixes.

---

## 🚀 Key Improvements & Fixes (✿◕‿◕)

- **1.21+ Compatibility**: Optimized dependencies to target `1.21` Paper APIs, ensuring forward-compatibility with all newer Minecraft versions.
- **PlaceholderAPI Color Resolution**: Resolved issues where legacy colors and custom hex codes expanded from PAPI placeholders (like custom group suffixes) would print as raw, unformatted text in chat.
- **Style Leakage Fixes**: Fixed a critical Minecraft rendering bug where styles like `&k` (obfuscated/enchanted text), bold `&l`, or italic `&o` would leak past color changes and corrupt player names. Pre-pending resets to all color codes ensures correct Minecraft legacy color formatting behavior in MiniMessage.

---

## 🧩 Compatibility (｀・ω・´)

| Component | Support Status |
|---|---|
| **Minecraft** | `1.21` and all newer versions (`1.21+`) |
| **Server Platform** | Paper, Folia, or Spigot |
| **Java Version** | `25+` |

---

## 🔧 Requirements (◕‿◕)

- [LuckPerms](https://luckperms.net/) *(Required)* – Permissions plugin
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) *(Optional)* – Additional placeholders

---

## ✅ Features (o^▽^o)

- **MiniMessage Format**: Complete MiniMessage support with group- and track-specific formats.
- **Social Upgrades**: Mention pings, clickable links, and customizable emoji shortcuts (e.g. `:heart:` → ❤).
- **Integrated Moderation**: Toggleable anti-spam cooldown, repeated-message blocker, caps filter, profanity filter, and anti-advertising.
- **Integrated Mutes**: Full mute system with `/joeheslpc mute <player>` supporting custom durations.

---

## ⌨️ Commands & Permissions (✿◠‿◠)

| Command | Permission | Description |
|---|---|---|
| `/joeheslpc reload` | `joeheslpc.reload` | Reloads the configuration |
| `/joeheslpc version` | – | Shows the current installed version |
| `/joeheslpc help` | – | Displays available commands |
| `/joeheslpc mute <player> [duration]` | `joeheslpc.mute` | Mutes a player (e.g. `10m`, `2h`, or permanent) |
| `/joeheslpc unmute <player>` | `joeheslpc.mute` | Unmutes a player |

---

## ⚙️ Configuration (`config.yml`) (✿ >‿<)

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

# Allow <gradient> / <rainbow> for players with joeheslpc.chatcolor
allow-gradient-tags: true

# Worlds where JoehesLPC does NOT format chat
disabled-worlds: []

# Check Modrinth for updates on startup
update-checker: true

# Reload message
reload-message: "<green>Reloaded JoehesLPC configuration!"
```

---

## 🛠️ Building (￣▽￣)

```bash
./gradlew shadowJar
# output: build/libs/JoehesLPC-1.21.11.jar
```
