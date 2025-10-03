# ESM AI Tweaks

Changes AI behavior added by Epic Siege Mod and introduces few other additions/fixes to make it more "RotN-Compatible".

Requires the following mod(s) to be enabled:
- Epic Siege Mod (13.169)

Initially made for RotN, but can be used by itself and with other mods.


## Mod Info

**TL;DR : The purpose of this mod is to make digging mobs respect block mining tiers added by ToolProgression.**<br>

In RotN 3.3 (and possibly previous versions), mobs that can dig (using Epic Siege Mod AI) will be 
able to mine any breakable/non-blacklisted block, as long as the tool category matches.<br>
However, RotN uses ToolProgression, which may enable a block to be mined by the player 
only if the held tool tier matches, or is higher than the blocks's tier.<br>
Digging mobs on the other hand do not follow this rule, and can for instance (if given enough time), 
break an obsidian block with a wooden pickaxe, which is how it works in vanilla.

The goal was to force those mobs to follow the same mining rules as the player does. 
To achieve this, Epic Siege Mod digging AI has been integrated into this mod and slighly edited 
to implement required checks (preventing mobs breaking blocks of higher tier than their held hold), 
and to improve compatibility between Epic Siege Mod and ToolProgression.


## Main Features

The following AI-related changes are exclusively for the digging AI.

- **Digging mobs respect the block mining level/tier (ToolProgression).**<br>
They will not be able to mine a block with their held tool if it does not fulfill 
said block requirements (as it does for the player).<br>

- **Digging mobs mining checks are done for both main and off hand items.**<br>
If the block requires a tool to be mined, both held items will be used for the checks.<br>
Can be toggled off in the config, to only check main hand.

- **Digging mobs being given extra chances to get an iron pickaxe is configurable.**<br>
Epic Siege Mod native digging AI adds an approximate 1/4 chance for them to be given this tool on spawn. 
Since other mods in RotN may already add such features ***(needs checking)***, 
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

## Planned Features and Fixes

[**TOP**] [FIX] Server crash due to client side package access (l18n).

No plans to implement all of Epic Siege Mod AI or content into this mod as of now, 
but it is something that can be achieved.


## Development Notes

Since this mod has been developed in VSCode, compatibiliy with other IDE is not guaranteed, 
but gradle wrapper tasks should work fine in most CLI, 
as long as the required assets to download for the first time build are reacheable 
(internet connection).

#### Compiling the mod
```bash
./gradlew build
```

#### Running Minecraft from the project
```bash
./gradlew runclient
```

Extra Mods to test with can either be put in the `libs/` folder or added from the `build.gradle` file.

#### Extra Notes

ForgeGradle version used here is outdated (*2.3-SNAPSHOT*) and not supported anymore. 
Various issues have taken place trying to make this project work with newer ones, 
and the final decision was to stick with it.
For that reason, it currently does not seem possible to use the latest *1.12.2* forge build (*14.23.5.2860*) 
when running Minecraft from gradlew. 
This project currently uses the latest compatible forge build.<br>
The compiled mod still works fine with both forge versions. 
See [gradle.properties](/gradle.properties) for all the versions used in the project.<br>
Other issues may still arise while attempting to run the client from IDE 
(such as minecraft not finding assets to download due to invalid URLs).


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