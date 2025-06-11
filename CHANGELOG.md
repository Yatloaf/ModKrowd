# ModKrowd changelog

## 0.2.2: Unreleased

### Added

- Tie Detector feature.
- Indicators for custom messages added by the mod.
- MissileWars death message theming.

### Changed

- Separate Chat History messages are now translated.
- Renamed Dejoin to Persistent Chat Messages.
  - The old name was analogous to Deconfigure, a feature that was removed before release.
  - The new name is analogous to Separate Chat History.

### Fixed

- Disabling a theme not invalidating the tab list cache.
- Unused translation keys.

## 0.2.1: 2025-05-01

### Added

- Themes can now tint the tab list background.

### Changed

- High Contrast Theme now uses custom colors:
  - Pink for Respected and AFK (previously light purple).
  - Lavender for Veteran (previously yellow).
- High Contrast Theme now styles join/leave and AFK messages more distinctly.

### Fixed

- Slapfish not being recognized.
- Fishslap and Slapfish chat messages not being themed correctly.
- High Contrast Theme not modifying rank color in tab header.
- Message Preview showing empty chat message on unknown servers.

## 0.2.0: 2025-04-05

### Added

- You’ve Got Mail feature.
- All known censored words for Message Preview.
- GameLobby tab list theming.
- `Connecting to <server>...` message theming.

### Changed

- `FeatureExtender` is now a proper entrypoint with the key `modkrowd:feature_extender`.
- Messages now get logged without modifications even if they are modified or blocked.
- Using the wrong Minecraft version no longer silently fails.

### Fixed

- Slim Armor sometimes being broken until reloading resources.
- Separate Chat History applying too late.
- Rare crash when trying to switch MissileWars lobbies while not ingame.
- Message Preview censoring being case-sensitive.
- High Contrast theme inconsistently styling AFK players.
- Early messages sometimes not being themed.

## 0.1.3: 2025-03-08

- Update to 1.21.4.

### Changed

- Creative predicate now passes on other servers with permission level 2 or higher.

### Removed

- Tab Hats, made obsolete by the fix of [MC-71990](https://bugs.mojang.com/browse/MC/issues/MC-71990).

### Fixed

- Message Preview coloring direct messages wrong.
- Message Preview mishandling looping direct messages.

## 0.1.2: 2025-02-05

### Added

- Cherry Lite theme (suggested by DIMM4).
- Tooltips for Separate Chat History and Autoswitch delay.

### Changed

- Separate Chat History rework, now respects singleplayer.

### Fixed

- Tab list not always updated.
- *Separate* typo.

## 0.1.1: 2025-01-11

### Fixed

- Wrong allowed predicates for Uninvisibility.
- No upper bound on Autoswitch delay.

## 0.1.0: 2025-01-08

- Initial release.