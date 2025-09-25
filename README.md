# ESM AI Tweaks

Changes AI behavior added by Epic Siege Mod to make it more "RotN-Compatible".

This mod requires the following mod(s) to be enabled:
- Epic Siege Mod (13.169)

It is tailored to work with RotN, but can be used by itself and with other mods.


## Mod Info

**TL;DR :** Makes digging mobs respect block mining tiers added by ToolProgression.<br>

In RotN 3.3 (and possibly previous versions), mobs that can dig (using Epic Siege Mod AI) will be able to mine any breakable/non-blacklisted block, as long as the tool category matches.<br>
However, RotN uses ToolProgression, which may enable a block to be mined by the player only if the held tool tier matches, or is higher than the blocks's tier.<br>
Digging mobs on the other hand do not follow this rule, and can for instance (if given enough time), break an obsidian block with a wooden pickaxe, which is how it works in vanilla.

The goal was to force those mobs to follow the same mining rules as the player does. To achieve this, Epic Siege Mod digging AI has been integrated into this mod and slighly edited to implement required checks (preventing mobs breaking blocks higher tier than their held hold), and to improve compatibility between Epic Siege Mod and ToolProgression.


## Main Features

The following AI-related changes are exclusively for the digging AI.

- **Digging mobs respect the block mining level/tier.**<br>
They will not be able to mine a block with their held tool if it does not fulfill said block requirements (as it does for the player).<br>

- **Digging mobs mining checks are done for both main and off hand items.**<br>
If the block requires a tool to be mined, both held items will be used for the checks.<br>
Can be toggled off in the config, to only check main hand.

- **Digging mobs are no longer given extra chances to get an iron pickaxe.**<br>
Epic Siege Mod native digging AI adds an approximate 1/4 chance for them to be given this tool on spawn. Other mods already add such feature ***(needs checking)***, with a larger and more configurable ***(?)*** modpack-related tools variety.

- **Digging AI can be switched back to Epic Siege Mod's original in the config.**<br>
If enabled in the config, digging mobs will behave the same way they did without this mod.
The world/server must not be running for this setting to be changed in-game and for changes to take effect.

- **Block Breaking Texture dissapears when the block isnt mined.**<br>
The texture is properly updated if the mob stops breaking a block for any reason. It used to sometimes be stuck at the last mining progress until the block was updated by other means. (visual only)


## Planned Features and Fixes

[**TOP**] [FIX] Make mobs stop breaking the block when hit or moved.<br>

[**MED**] [FIX] Make the config toggle for mobRespectMiningTier work.<br>
[**MED**] [FEAT] Make gradle remove logger.debug lines when buliding .jar dist.<br>

[**LOW**] [FEAT] Add a config toogle to get back the pickaxe chance on spawn.<br>
[**LOW**] [FEAT] Make the pickaxe chance on spawn configurable if enabled.<br>

No plans to implement more of Epic Siege Mod AI or content into this mod, but it is something that can be achieved.


## Development Notes

Since this mod has been developed in VSCode, compatibiliy with other IDE is not guaranteed, but gradle wrapper tasks should work fine in most CLI, as long as the required assets to download for the first time build are reacheable (internet connection).

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

ForgeGradle version used here is outdated (*2.3-SNAPSHOT*) and not supported anymore. Various issues have taken place trying to make this project work with newer ones, and the final decision was to stick with it.
For that reason, it currently does not seem possible to use the latest *1.12.2* forge build (*14.23.5.2860*) when running Minecraft from gradlew. This project currently uses the latest compatible forge build.<br>
The compiled mod still works fine with both forge versions. See [gradle.properties](/gradle.properties) for all the versions used in the project.<br>
Other issues may still arise while attempting to run the client from IDE (such as minecraft not finding assets to download due to invalid URLs).


## Links

**Modpack** <br>
&nbsp; Rebirth of the Night | | [Curseforge](https://www.curseforge.com/minecraft/modpacks/rebirth-of-the-night) | [Github](https://github.com/Rebirth-of-the-Night/Rebirth-Of-The-Night) | <br>


**Mods** <br>
&nbsp; Epic Siege Mod | | [Curseforge](https://www.curseforge.com/minecraft/mc-mods/epic-siege-mod) | [Github](https://github.com/da3dsoul/Epic-Siege-Mod) | <br>
&nbsp; ToolProgression | | [Curseforge](https://www.curseforge.com/minecraft/mc-mods/tool-progression) | [Github](https://github.com/tyra314/ToolProgression) | <br>


## License

[MIT License](LISENCE)