# ESM AI Tweaks (1.12)

Changes AI behavior added by [Epic Siege Mod](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) and introduces few other additions/fixes to make it more "RotN-Compatible".
Initially made for Rebirth of the Night, but can be used by itself and with other mods.

Requires the following mod(s) to be enabled:
- Epic Siege Mod (13.169)

ToolProgression (1.12.2-1.6.12) is not required, but highly recommended, to make all the settings work properly.


## Mod Info

**TL;DR : The purpose of this mod is to make digging mobs respect block mining tiers added by ToolProgression.**<br>

In Rebirth of the Night 3.3 (and possibly previous versions), mobs that can dig (using Epic Siege Mod AI) will be 
able to mine any breakable/non-blacklisted block, as long as the tool category matches.<br>
However, Rebirth of the Night uses ToolProgression, which may enable a block to be mined by the player 
only if the held tool tier matches, or is higher than the blocks's tier.<br>
Digging mobs on the other hand do not follow this rule, and can for instance (if given enough time), 
break an obsidian block with a wooden pickaxe, which is how it works in vanilla.

The goal was to force those mobs to follow the same mining rules as the player does. 
To achieve this, Epic Siege Mod digging AI has been integrated into this mod and slighly edited 
to implement required checks (preventing mobs breaking blocks of higher tier than their held hold), 
and to improve compatibility between Epic Siege Mod and ToolProgression.


## Main Features

The following AI-related changes are exclusively for the digging AI.

- **Digging mobs respect block mining levels/tiers added by ToolProgression.**<br>
They will not be able to mine a block with their held tool if it does not fulfill 
said block requirements (as it does for the player).<br>

- **Digging mobs mining checks can be done for both main and off hand items.**<br>
If the block requires a tool to be mined, both held items will be used for the checks.<br>
Can be toggled off in the config, to only check main hand.

- **Digging mobs being given extra chances to get an iron pickaxe is configurable.**<br>
Epic Siege Mod native digging AI adds an approximate 1/4 chance for them to be given this tool on spawn. 
Since other mods in Rebirth of the Night may already add such features ***(needs checking)***, 
with a larger and more configurable ***(?)*** modpack-related tools variety,
this feature is now disabled by default but can be re-enabled in this mod's config. 
The chance to get a pickaxe can be edited as well.

- **Digging AI can be switched back to Epic Siege Mod's original behavior.**<br>
Can be changed in the config by disabling this mod's digging AI. Digging mobs will behave the same way they would without this mod.
The world/server must not be running for this setting to be changed in-game 
and for changes to take effect.

- **Block breaking texture goes away when the block isnt mined.**<br>
The texture is properly updated if the mob stops breaking a block for any reason. 
It used to sometimes be stuck at the last mining progress until the block was updated by other means. 
(visual only)

<br>
Few other minor changes and improvements have been made, such as displaying block and tool infos in-game for block breaking tests and debugging, or fixing the AI assignement process being sometimes done twice for each entity.


## Installation

Download `esmtweaks-1.12-xxx.jar` file from [Releases](https://github.com/lirkas/esm-tweaks/releases/latest) and put it in the `minecraft/mods` folder.<br>
`esmtweaks-1.12-xxx-esm168.jar` is for Epic Siege Mod versions 13.167 / 13-168 (and possibly lower).


## Planned Features and Fixes

See [Todo](/docs/TODO.md).


## Development Infos

See [DevNotes](/docs/DEVINFO.md) for details on how to compile this mod, and other infos about it.


## Links

**Modpack** <br>
&nbsp; `Rebirth of the Night` | | 
[Curseforge](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) | 
[Github](https://github.com/Rebirth-of-the-Night/Rebirth-Of-The-Night) | <br>


**Mods** <br>
&nbsp; `Epic Siege Mod` | | 
[Curseforge](https://www.curseforge.com/minecraft/mc-mods/epic-siege-mod) | 
[Github](https://github.com/da3dsoul/Epic-Siege-Mod) | <br>
&nbsp; `ToolProgression` | | 
[Curseforge](https://www.curseforge.com/minecraft/mc-mods/tool-progression) | 
[Github](https://github.com/tyra314/ToolProgression) | <br>


## License

[MIT License](LISENCE)